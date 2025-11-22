package com.example.hackerton_be.User.repository;

import com.example.hackerton_be.User.database.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<Users, Long> {

    Optional<Users> findByUserId(String userId);
}
