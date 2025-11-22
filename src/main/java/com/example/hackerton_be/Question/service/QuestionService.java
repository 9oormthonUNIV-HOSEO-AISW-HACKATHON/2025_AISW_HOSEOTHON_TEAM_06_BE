package com.example.hackerton_be.Question.service;

import com.example.hackerton_be.Question.Dto.QuestionAnswerDto;
import com.example.hackerton_be.Question.Dto.QuestionDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QuestionService {

    /**
     * 기능 1: 모든 퀴즈 문제 목록을 불러옵니다.
     */
    List<QuestionDto> getAllQuestions();

    /**
     * 기능 2: 사용자의 퀴즈 정답을 체크하고, 통계를 업데이트합니다.
     */
    boolean checkAnswerAndUpdateStats(String userId, QuestionAnswerDto requestDto);
}