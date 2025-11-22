package com.example.hackerton_be.User.controller;

import com.example.hackerton_be.User.Dto.*;

import com.example.hackerton_be.User.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

@Tag(name="회원 인증", description = "회원 인증 관련 API 명세서입니다.")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody UserSignDto requestDto) {
        UserDto response = userService.signUp(requestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * 로그인 및 JWT 발급
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto requestDto, HttpServletResponse response) {
        String tokenResponse = userService.login(requestDto, response);
        return ResponseEntity.ok(tokenResponse);
    }

    /**
     * 아이디 중복 체크
     * */
    @PostMapping("/checkId")
    public ResponseEntity<String> checkId(@RequestBody UserCheckIdDto requestDto) {
        String userId = requestDto.getUserId();

        if (userService.checkUserIdD(userId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 아이디입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 아이디입니다.");
        }
    }

    /**
     * 마이페이지
     * */
    @GetMapping("/myPage")
    public ResponseEntity<UserSignDto> myPage(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = authentication.getName();
        System.out.println("dddd: "+authentication.getPrincipal());

        UserSignDto userDetails = userService.myPage(userId);

        return ResponseEntity.ok(userDetails);
    }

    /**
     * 유저 정보 업데이트
     * */
    @PostMapping("/updateUser")
    public ResponseEntity<String> updateUser(@RequestBody UserSignDto requestDto, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userId = authentication.getName();

        userService.updateUser(userId, requestDto);

        return ResponseEntity.ok("성공적으로 완료됐습니다.");
    }

    /**
     * 회원 탈퇴
     * */
    @PostMapping("/delUser")
    public ResponseEntity<String> delUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = authentication.getName();

        // 서비스 호출하여 사용자 삭제
        userService.deleteUser(userId);

        // 🌟 탈퇴 후 JWT 쿠키 무효화: 클라이언트 측에서 쿠키 삭제를 유도
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletResponse response = attributes.getResponse();
            if (response != null) {
                Cookie cookie = new Cookie("jwt", null);
                cookie.setMaxAge(0); // 즉시 만료
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }

        return ResponseEntity.ok("성공적으로 탈퇴되었습니다.");
    }

    /**
     * 사용자 포인트 조회
     * */
    @GetMapping("/myPoint")
    public ResponseEntity<String> myPoint(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 사용자입니다.");
        }

        String userId = authentication.getName();

        try {
            Integer point = userService.getUserPoint(userId);
            return ResponseEntity.ok(String.valueOf(point));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("인증 정보를 찾을 수 없습니다.");
        }
    }

    /**
     * 포인트 증감 (인풋: 점수만)
     */
    @PostMapping("/addPoint")
    public ResponseEntity<String> addPoint(
            @RequestBody UserPointDto requestBody,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = authentication.getName();

        Integer pointChange;
        try {
            if (requestBody.getUserPoint() == null || requestBody.getUserPoint().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("필수 매개변수가 누락됐습니다.");
            }
            pointChange = Integer.parseInt(requestBody.getUserPoint());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("포인트 값은 숫자로 입력되어야 합니다.");
        }

        try {
            userService.updatePoint(userId, pointChange);
            return ResponseEntity.ok("포인트가 성공적으로 업데이트되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}