package kr.co.milionvolt.ifive.service.user;

import kr.co.milionvolt.ifive.domain.user.LoginDTO;
import kr.co.milionvolt.ifive.domain.token.TokenResponseDTO;
import kr.co.milionvolt.ifive.domain.user.UserDetailsVO;
import kr.co.milionvolt.ifive.entity.RefreshTokenRedis;
import kr.co.milionvolt.ifive.exception.LoginException;
import kr.co.milionvolt.ifive.mapper.UserMapper;
import kr.co.milionvolt.ifive.repository.RefreshTokenRepository;
import kr.co.milionvolt.ifive.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginAndLogoutServiceImpl implements LoginAndLogoutService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserMapper userMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenResponseDTO authenticate(LoginDTO loginDTO) throws LoginException {
        try {
            // 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUserId(),
                            loginDTO.getPassword()
                    )
            );

            // 인증 성공 후 사용자 정보 조회
            UserDetailsVO detailsVO = userMapper.findByUserId(loginDTO.getUserId());
//            if (detailsVO == null) {
//                return new LoginException("사용자를 찾을 수 없습니다.");
//            }

            // Access Token 및 Refresh Token 생성
            String accessToken = tokenProvider.generateAccessToken(detailsVO.getId(), detailsVO.getRole());
            String refreshToken = tokenProvider.generateRefreshToken(detailsVO.getId());

            // Refresh Token 저장 (Redis, 사용자당 단일 토큰)
            RefreshTokenRedis tokenEntity = RefreshTokenRedis.builder()
                    .id(detailsVO.getId())
                    .token(refreshToken)
                    .expiryDate(LocalDateTime.now().plusSeconds(tokenProvider.getRefreshTokenExpiration() / 1000))
                    .ttl(tokenProvider.getRefreshTokenExpiration() / 1000) // TTL을 초 단위로 설정
                    .build();
            refreshTokenRepository.save(tokenEntity); // 기존 토큰 덮어쓰기

            log.debug("Refresh Token 저장 성공: {}", refreshToken);

            return TokenResponseDTO.builder()
                    .id(detailsVO.getId())
                    .username(detailsVO.getUserId())
                    .userId(detailsVO.getUserId())
                    .role(detailsVO.getRole())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (Exception ex) {
            log.error("로그인 실패: ", ex);
            throw new LoginException("로그인에 실패했습니다.", ex);
        }
    }

    @Override
    public void logout(String refreshToken) {
        
    }

    @Override
    public TokenResponseDTO refreshToken(String refreshToken) {
        return null;
    }

    @Override
    public void saveRefreshToken(String newRefreshToken, Integer id) {

    }


}


