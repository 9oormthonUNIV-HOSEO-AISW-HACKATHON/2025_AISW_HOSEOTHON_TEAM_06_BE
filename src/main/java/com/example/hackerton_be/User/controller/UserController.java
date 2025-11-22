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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="회원 인증", description = "회원 인증 관현 API 명세서입니다.")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    // 1. AuthService 의존성 주입 (이전 코드에서 누락된 부분)
    private final UserService userService;

    /**
     * 회원가입
     * @param requestDto 회원 정보 (ID, PW 등)
     * @return 성공적으로 생성된 회원 정보
     */
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody UserSignDto requestDto) {
        UserDto response = userService.signUp(requestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * 로그인 및 JWT 발급
     * @param requestDto 로그인 정보 (ID, PW)
     * @return JWT 토큰 및 메타데이터
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto requestDto, HttpServletResponse response) {
        String tokenResponse = userService.login(requestDto, response);
        return ResponseEntity.ok(tokenResponse);
    }

    /** 아이디 중복 체크 */
    @PostMapping("/checkId")
    public ResponseEntity<String> checkId(@RequestBody UserCheckIdDto requestDto) {
        String userId = requestDto.getUserId();

        if (userService.checkUserIdDuplication(userId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 아이디입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 아이디입니다.");
        }
    }
}