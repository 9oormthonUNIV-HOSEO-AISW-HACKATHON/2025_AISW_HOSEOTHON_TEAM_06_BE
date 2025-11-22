package com.example.hackerton_be.User.service;

//import com.example.hackerton_be.User.Dto.UserLoginDto;
//import com.example.hackerton_be.User.Login.CustomUserDetails;
import com.example.hackerton_be.User.Login.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.example.hackerton_be.User.database.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    static final SecretKey key =
            Keys.hmacShaKeyFor(Decoders.BASE64.decode(
                    "jwtpassword123jwtpassword123jwtpassword123jwtpassword123jwtpassword" // <- 이 친구는 내가 알아서 설정 가능
            ));

    // JWT 만들어주는 함수
    public static String createToken(Authentication auth) {
        CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
        Users user = customUserDetails.getUser();
        var authorities = auth.getAuthorities().stream().map(a -> a.toString())
                .collect(Collectors.joining(","));
        String jwt = Jwts.builder()
                .claim("userId", user.getUserId()) // 내가 토큰에 넣고 싶은 정보들
                .claim("authorities", authorities)
                .issuedAt(new Date(System.currentTimeMillis())) // 언제 생성됨?
                .expiration(new Date(System.currentTimeMillis() + 3600000L))
                .signWith(key) // 해싱키
                .compact();
        return jwt;
    }

    // JWT 까주는 함수
    public static Claims extractToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();
        return claims;
    }
}
