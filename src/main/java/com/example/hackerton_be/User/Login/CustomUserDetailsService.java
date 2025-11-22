package com.example.hackerton_be.User.Login;

import com.example.hackerton_be.User.database.Users;
import com.example.hackerton_be.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        Users user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        // Users 엔티티를 UserDetails 타입으로 변환하는 CustomUserDetails 객체를 반환해야 합니다.
        return new CustomUserDetails(user); // 아래 3번 항목 구현 필요
    }
}
