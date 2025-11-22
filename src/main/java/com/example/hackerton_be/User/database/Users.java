package com.example.hackerton_be.User.database;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "members")
@Getter
@Setter
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "members_idx", nullable = false)
    private Long idx;

    @Column(name = "user_id", length = 100, nullable = false, unique = true)
    private String userId;

    @Column(name = "user_pass", length = 255, nullable = false)
    private String userPass;

    @Column(name = "user_isMz", length = 1, nullable = false)
    private String userIsMz;

    @Column(name = "user_point", nullable = false)
    private Integer userPoint;

    @Column(name = "user_name", length = 20, nullable = false)
    private String userName;

    @Column(name = "user_nickname", length = 20)
    private String userNickname;

    @Column(name = "user_sex", length = 1)
    private String userSex;
}
