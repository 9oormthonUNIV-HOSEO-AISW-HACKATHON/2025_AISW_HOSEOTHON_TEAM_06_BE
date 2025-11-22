package com.example.hackerton_be.User.Impl;

import com.example.hackerton_be.User.Dto.UserDto;
import com.example.hackerton_be.User.Dto.UserLoginDto;
import com.example.hackerton_be.User.Dto.UserSignDto;
import com.example.hackerton_be.User.database.Users;
import com.example.hackerton_be.User.repository.UserRepository;

import com.example.hackerton_be.User.service.JwtUtil;
import com.example.hackerton_be.User.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    // private final AuthenticationManager authenticationManager; // 삭제

    // AuthenticationConfiguration을 주입
    private final AuthenticationConfiguration authenticationConfiguration;

    /**
     * 회원 가입 로직
     */
    @Transactional
    @Override
    public UserDto signUp(UserSignDto requestDto) {

        // 유효성 검증: ID 중복 확인
        if (userRepository.findByUserId(requestDto.getUserId()).isPresent()) {
            throw new RuntimeException("이미 사용 중인 아이디입니다.");
        }

        // 비밀번호 암호화
        // 주의: Users 엔티티에 @Builder와 @AllArgsConstructor가 필요합니다.
        String encodedPass = passwordEncoder.encode(requestDto.getUserPass());

        // DTO -> Entity 변환 및 저장
        Users user = new Users();
        user.setUserId(requestDto.getUserId());
        user.setUserPass(encodedPass);
        user.setUserIsMz(requestDto.getUserIsMz());
        user.setUserName(requestDto.getUserName());
        user.setUserSex(requestDto.getUserSex());
        user.setUserPoint(0); // 초기 포인트 설정

        Users savedUser = userRepository.save(user);

        // 응답 DTO 반환
        return new UserDto(savedUser.getIdx(), savedUser.getUserId());
    }

    /**
     * 로그인 및 토큰 발급
     */
//    @Transactional(readOnly = true)
    @Override
    public String login(UserLoginDto requestDto, HttpServletResponse response) {

        try {
            // 1. AuthenticationManager를 AuthenticationConfiguration에서 가져옴
            AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager(); // <-- 변경된 부분

            // 2. 인증 정보 객체 (Token) 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(requestDto.getUserId(), requestDto.getUserPass());

            // 3. AuthenticationManager를 통해 인증 시도
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 4. 인증 성공 후, SecurityContext에 Authentication 객체 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 4. JWT 생성
            String token = jwtUtil.createToken(authentication);

            System.out.println("token = " + token);

            // 쿠키 생성 및 반환 로직은 그대로 유지
            var cookie = new Cookie("jwt", token);
            cookie.setMaxAge(10);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            // 반환 타입이 TokenDto가 아닌 String이므로 반환 값도 수정해야 합니다.
            // return new TokenDto("Bearer", token, 3600L); (원래 방식)
            return token; // 변경된 반환 타입에 맞춤
        }catch (Exception e) {
            // 인증 실패 (아이디/비밀번호 불일치) 또는 다른 오류 처리
            throw new RuntimeException("로그인 실패: 아이디 또는 비밀번호를 확인하거나, 서버 오류.", e);
        }
    }
}