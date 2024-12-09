package kr.co.milionvolt.ifive.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 허용할 출처 설정 (특정 출처만 허용)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));

        // 허용할 HTTP 메소드 설정
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // 허용할 헤더 설정
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Refresh-Token"));

        // 자격 증명(쿠키, 인증 헤더 등)을 포함할지 여부
        configuration.setAllowCredentials(true);

        // 설정을 적용할 URL 패턴 지정
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);

        return source;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                // 정적 자원은 시큐리티를 적용하지 말라 는 의미
                // 정적자원 범위 : 이미지,css,js, .jar (html 은 없음)
                .requestMatchers("templates/*.html", "/html/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS 및 CSRF 설정
                .cors().and()
                .csrf(csrf -> csrf.disable())

                // 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/uploads/**").permitAll() // 이미지 파일에 대한 접근 허용
                        .requestMatchers("/api/v1/login/**", "/api/v1/signup/**", "/api/v1/find/**", "/api/v1/reset/**", "/api/v1/logout/**", "/api/v1/main/**", "/api/v1/search/**", "/main/**","/charging/**",  "/api/v1/info/**").permitAll() // 로그인 및 회원가입 엔드포인트는 누구나 접근 가능
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN") // 'ADMIN' 역할만 접근 가능
                        .anyRequest().authenticated() // 나머지 모든 요청은 인증 필요
                );

        return http.build();
    }

    // AuthenticationManager 빈 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 비밀번호 인코더 빈 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
