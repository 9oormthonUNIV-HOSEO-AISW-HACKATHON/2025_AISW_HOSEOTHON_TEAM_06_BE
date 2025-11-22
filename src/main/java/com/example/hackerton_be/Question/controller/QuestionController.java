package com.example.hackerton_be.Question.controller;

import com.example.hackerton_be.Question.Dto.QuestionDto;
import com.example.hackerton_be.Question.Dto.QuestionAnswerDto;
import com.example.hackerton_be.Question.service.QuestionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="퀴즈", description = "퀴즈 관련 API 명세서입니다.")
@RestController
@RequestMapping("/api/que")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    /**
     * 모든 퀴즈 문제 불러오기 (인증 필요)
     */
    @GetMapping("/list")
    public ResponseEntity<List<QuestionDto>> getAllQuestions() {
        List<QuestionDto> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    /**
     * 퀴즈 정답 체크 및 통계 업데이트 (인증 필요)
     */
    @PostMapping("/check")
    public ResponseEntity<String> checkAnswer(
            @RequestBody QuestionAnswerDto requestDto,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증이 필요합니다.");
        }

        String userId = authentication.getName();

        try {
            boolean isCorrect = questionService.checkAnswerAndUpdateStats(userId, requestDto);

            if (isCorrect) {
                return ResponseEntity.ok("정답입니다!");
            } else {
                return ResponseEntity.ok("오답입니다.");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}