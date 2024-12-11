package kr.co.milionvolt.ifive.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.milionvolt.ifive.domain.token.TokenUserInfoDTO;
import kr.co.milionvolt.ifive.domain.user.LoginDTO;
import kr.co.milionvolt.ifive.domain.user.TokenResponseDTO;
import kr.co.milionvolt.ifive.security.JwtTokenProvider;
import kr.co.milionvolt.ifive.service.user.LoginAndLogoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class LoginAndLogoutController {

    private final LoginAndLogoutService loginAndLogoutService;
    private final JwtTokenProvider tokenProvider;


    @GetMapping("/info")
    public ResponseEntity<TokenUserInfoDTO> userInfo(@RequestBody String accessTokenDTO, HttpServletRequest request) {
        TokenUserInfoDTO memberInfoDTO = loginAndLogoutService.userInfo(accessTokenDTO);
        return new ResponseEntity<>(memberInfoDTO, HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        try {
            TokenResponseDTO tokenResponseDTO = loginAndLogoutService.authenticate(loginDTO);
            // Refresh Token을 HttpOnly 쿠키에 설정 (SameSite 속성 포함)
            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", tokenResponseDTO.getRefreshToken())
                    .httpOnly(true)
                    .secure(false) // 개발 환경에서는 false, 배포 시 true
                    .path("/")
                    .maxAge(tokenProvider.getRefreshTokenExpiration() / 1000)
                    .sameSite("Lax")
                    .build();
            response.addHeader("Set-Cookie", refreshCookie.toString());

            // Access Token은 JSON 응답으로 전달
            TokenResponseDTO responseBody = TokenResponseDTO.builder()
                    .id(tokenResponseDTO.getId())
                    .userId(tokenResponseDTO.getUserId())
                    .role(tokenResponseDTO.getRole())
                    .accessToken(tokenResponseDTO.getAccessToken())
                    .build();

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            log.error("로그인 실패: ", e); // 예외의 스택 트레이스를 함께 로그에 남김
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인에 실패했습니다.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken != null && !refreshToken.isEmpty()) {
            loginAndLogoutService.logout(refreshToken);

            // Refresh Token 쿠키 삭제 (SameSite 속성 포함)
            ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                    .httpOnly(true)
                    .secure(true) // HTTPS 사용 시 true
                    .path("/")
                    .maxAge(0)
                    .sameSite("Lax")
                    .build();
            response.addHeader("Set-Cookie", deleteCookie.toString());

            return ResponseEntity.ok().body("로그아웃 되었습니다.");
        }
        return ResponseEntity.badRequest().body("Refresh Token이 필요합니다.");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Refresh Token이 필요합니다.");
        }

        try {
            TokenResponseDTO tokenResponseDTO = loginAndLogoutService.refreshToken(refreshToken);

            // 새로운 Refresh Token 생성 및 업데이트
            String newRefreshToken = tokenProvider.generateRefreshToken(tokenResponseDTO.getId());
            loginAndLogoutService.logout(refreshToken); // 기존 Refresh Token 삭제
            loginAndLogoutService.saveRefreshToken(newRefreshToken, tokenResponseDTO.getId()); // 새로운 Refresh Token 저장

            // 새로운 Refresh Token을 HttpOnly 쿠키에 설정 (SameSite 속성 포함)
            ResponseCookie newRefreshCookie = ResponseCookie.from("refresh_token", newRefreshToken)
                    .httpOnly(true)
                    .secure(false) // HTTPS 사용 시 true, 현재는 HTTP 사용
                    .path("/")
                    .maxAge(tokenProvider.getRefreshTokenExpiration() / 1000)
                    .sameSite("Lax")
                    .build();
            response.addHeader("Set-Cookie", newRefreshCookie.toString());

            // Access Token은 JSON 응답으로 전달
            TokenResponseDTO responseBody = TokenResponseDTO.builder()
                    .id(tokenResponseDTO.getId())
                    .userId(tokenResponseDTO.getUserId())
                    .role(tokenResponseDTO.getRole())
                    .accessToken(tokenResponseDTO.getAccessToken())
                    .build();

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            log.error("토큰 갱신 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
