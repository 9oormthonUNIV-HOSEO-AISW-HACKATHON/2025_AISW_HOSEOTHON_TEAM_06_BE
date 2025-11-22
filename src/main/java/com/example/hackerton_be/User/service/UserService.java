package com.example.hackerton_be.User.service;

import com.example.hackerton_be.User.Dto.UserDto;
import com.example.hackerton_be.User.Dto.UserLoginDto;
import com.example.hackerton_be.User.Dto.UserSignDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface UserService {
    // 회원 가입
    UserDto signUp(UserSignDto requestDto);

    // 로그인 및 토큰 발급
    String login(UserLoginDto requestDto, HttpServletResponse response);

    // 아이디 체크
    boolean checkUserIdDuplication(String userId);
}
