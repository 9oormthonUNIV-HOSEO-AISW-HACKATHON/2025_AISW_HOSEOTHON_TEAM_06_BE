package com.example.hackerton_be.User.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignDto {
    private String userId;
    private String userPass;
    private String userIsMz;
    private String userName;
    private String userSex;
}
