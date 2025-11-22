package com.example.hackerton_be.User.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto {
    private Long idx; // PK 이름에 맞게 수정
    private String userId;
}