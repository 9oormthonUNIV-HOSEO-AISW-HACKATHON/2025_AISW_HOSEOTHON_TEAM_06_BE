package com.example.hackerton_be.Question.repository;

import com.example.hackerton_be.Question.database.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
