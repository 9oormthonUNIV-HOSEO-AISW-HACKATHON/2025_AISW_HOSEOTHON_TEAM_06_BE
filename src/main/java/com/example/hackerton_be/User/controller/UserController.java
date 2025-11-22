package com.example.hackerton_be.User.controller;

import com.example.hackerton_be.User.Dto.UserCheckIdDto;
import com.example.hackerton_be.User.Dto.UserDto;

import com.example.hackerton_be.User.Dto.UserLoginDto;
import com.example.hackerton_be.User.Dto.UserSignDto;
import com.example.hackerton_be.User.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@Tag(name="회원 인증", description = "회원 인증 관현 API 명세서입니다.")
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

    @PostMapping("/updateUser")
    public ResponseEntity<String> updateUser(@RequestBody UserSignDto requestDto, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok("성공적으로 완료됐습니다.");
    }
}