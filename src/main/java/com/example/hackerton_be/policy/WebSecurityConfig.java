package com.example.hackerton_be.policy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF 비활성화 (REST API 표준)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. 세션 STATELESS 설정 (JWT 사용 시 필수)
                // authorizeHttpRequests 블록 밖으로 이동
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // 3. Swagger UI 및 API 문서 경로 접근 허용
                        .requestMatchers(
                                "/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-ui/",
                                "/v3/api-docs/**",
                                "/swagger-resources/**"
                        ).permitAll()

                        // 4. 인증/회원가입 API 접근 허용
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login"
                        ).permitAll()

                        .requestMatchers(
                                "/api/test/test1",
                                "/api/test/test2"
                        ).permitAll()

                        // 5. 나머지 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                );

        // 6. JWT 필터 등록 (이 코드는 실제 JWTUtil 구현 후 추가해야 합니다.)
        // http.addFilterBefore(new JwtFilter(jwtUtil, userService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}