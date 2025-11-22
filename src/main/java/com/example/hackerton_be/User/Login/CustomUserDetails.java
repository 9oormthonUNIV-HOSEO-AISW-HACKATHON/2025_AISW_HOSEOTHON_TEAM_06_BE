package com.example.hackerton_be.User.Login;

import com.example.hackerton_be.User.database.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


public class CustomUserDetails implements UserDetails {
    private final Users user;

    public CustomUserDetails(Users user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한은 임시로 "ROLE_USER"로 설정합니다.
        // 실제로는 DB에서 권한(예: user.getRole())을 가져와야 합니다.
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return user.getUserPass();
    }

    @Override
    public String getUsername() {
        return user.getUserId();
    }

    // 나머지 메서드는 true를 반환하여 계정 유효성을 기본적으로 통과시킵니다.
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    // JWT 생성 시 필요한 Users 엔티티를 반환하는 메서드 추가
    public Users getUser() {
        return user;
    }
}
