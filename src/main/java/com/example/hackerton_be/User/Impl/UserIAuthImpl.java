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
import org.springframework.context.annotation.Lazy; // Lazy import 추가
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserIAuthImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Lazy
    private final AuthenticationManager authenticationManager;

    /**
     * 회원 가입 로직
     */
    @Transactional
    @Override
    public UserDto signUp(UserSignDto requestDto) {
        if (userRepository.findByUserId(requestDto.getUserId()).isPresent()) {
            throw new RuntimeException("이미 사용 중인 아이디입니다.");
        }

        String encodedPass = passwordEncoder.encode(requestDto.getUserPass());

        Users user = new Users();
        user.setUserId(requestDto.getUserId());
        user.setUserPass(encodedPass);
        user.setUserIsMz(requestDto.getUserIsMz());
        user.setUserName(requestDto.getUserName());
        user.setUserSex(requestDto.getUserSex());
        user.setUserPoint(0);

        Users savedUser = userRepository.save(user);

        return new UserDto(savedUser.getIdx(), savedUser.getUserId());
    }

    /**
     * 로그인 및 토큰 발급
     */
    @Override
    public String login(UserLoginDto requestDto, HttpServletResponse response) {

        try {
            // 1. AuthenticationManager를 안전하게 가져옴
//            AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
            // 2. DTO를 사용해 인증 토큰 생성 (요청 객체 접근 오류 수정)
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(requestDto.getUserId(), requestDto.getUserPass());

            // 3. 인증 시도
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 4. SecurityContext에 Authentication 객체 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 5. JWT 생성
            String token = jwtUtil.createToken(authentication);

            System.out.println("token = " + token);

            var cookie = new Cookie("jwt", token);
            cookie.setMaxAge(10);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            return token;
        }catch (Exception e) {
            System.err.println("로그인 인증 실패: " + e.getClass().getName() + " - " + e.getMessage());
            throw new RuntimeException("로그인 실패: 아이디 또는 비밀번호를 확인하세요.", e);
        }
    }

    /**
     * 아이디 중복 체크
     */
    @Override
    public boolean checkUserIdD(String userId) {
        return userRepository.findByUserId(userId).isPresent();
    }

    /**
     * 마이페이지 상세 정보 조회 로직
     */
    @Transactional(readOnly = true)
    @Override
    public UserSignDto myPage(String userId){
        Users user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        // 2. 응답 DTO 생성 (필요한 모든 정보를 담아)
        return new UserSignDto(
                user.getUserId(), user.getUserPass(),
                user.getUserIsMz(), user.getUserName(),
                user.getUserSex(),user.getUserNickname()
        );
    }

    @Transactional
    @Override
    public UserDto updateUser(String userId, UserSignDto requestDto) {
        // 1. ID로 사용자 엔티티 조회 (수정 대상)
        Users user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        // 2. 요청 DTO를 기반으로 엔티티 필드 업데이트

        // 비밀번호가 제공된 경우에만 암호화하여 업데이트
        if (requestDto.getUserPass() != null && !requestDto.getUserPass().isEmpty()) {
            String encodedPass = passwordEncoder.encode(requestDto.getUserPass());
            user.setUserPass(encodedPass);
        }

        // 나머지 필드들은 null 체크 후 업데이트
        if (requestDto.getUserName() != null) user.setUserName(requestDto.getUserName());
        if (requestDto.getUserSex() != null) user.setUserSex(requestDto.getUserSex());
        if (requestDto.getUserIsMz() != null) user.setUserIsMz(requestDto.getUserIsMz());
        if (requestDto.getUserNickname() != null) user.setUserNickname(requestDto.getUserNickname());

        // 3. 변경 사항 저장 (Transactional 덕분에 자동 반영되지만 명시적으로 호출)
        Users updatedUser = userRepository.save(user);

        // 4. 응답 DTO 반환 (ID, PK만 포함)
        return new UserDto(updatedUser.getIdx(), updatedUser.getUserId());
    }
}