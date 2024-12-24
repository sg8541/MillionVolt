package kr.co.milionvolt.ifive.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    // Access Token 유효기간
    @Value("${jwt.access.token.expiration}")
    private Long accessTokenExpiration;

    // Refresh Token 유효기간
    @Getter
    @Value("${jwt.refresh.token.expiration}")
    private Long refreshTokenExpiration;

    private SecretKey getSecretKey() {
        // secret key 길이 보장
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Access Token 생성
    public String generateAccessToken(Integer id, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(String.valueOf(id))
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, getSecretKey())
                .compact();

        System.out.println("Generated Token: '" + token + "'");
        return token.trim();
    }

    // Refresh Token 생성
    public String generateRefreshToken(Integer id) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(String.valueOf(id)) // memberId를 String으로 변환하여 설정
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, getSecretKey())
                .compact();
    }

    // JWT 토큰에서 memberId 추출
    public Integer getIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSecretKey())
                .parseClaimsJws(token)
                .getBody();

        return Integer.parseInt(claims.getSubject());
    }

    // JWT 토큰 검증
    public boolean validateToken(String authToken) {
        try {
            log.info("authToken :  {}", authToken);
            Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            // 잘못된 JWT 서명
            System.err.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            // 잘못된 JWT 토큰
            System.err.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            // 만료된 JWT 토큰
            System.err.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            // 지원되지 않는 JWT 토큰
            System.err.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            // JWT 토큰이 비어있음
            System.err.println("JWT claims string is empty.");
        }
        return false;
    }

}
