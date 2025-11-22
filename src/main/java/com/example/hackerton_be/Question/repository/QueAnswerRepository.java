package com.example.hackerton_be.Question.repository;

import com.example.hackerton_be.Question.database.QueAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueAnswerRepository extends JpaRepository<QueAnswer, Long> {
}
