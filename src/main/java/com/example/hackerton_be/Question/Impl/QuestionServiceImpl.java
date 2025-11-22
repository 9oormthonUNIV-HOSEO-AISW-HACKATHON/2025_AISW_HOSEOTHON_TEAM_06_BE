package com.example.hackerton_be.Question.Impl;

import com.example.hackerton_be.Question.Dto.QuestionAnswerDto;
import com.example.hackerton_be.Question.Dto.QuestionDto;
import com.example.hackerton_be.Question.database.Question;
import com.example.hackerton_be.Question.database.QueAnswer;
import com.example.hackerton_be.Question.repository.QuestionRepository;
import com.example.hackerton_be.Question.repository.QueAnswerRepository;
import com.example.hackerton_be.Question.service.QuestionService;
import com.example.hackerton_be.User.database.Users;
import com.example.hackerton_be.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QueAnswerRepository queAnswerRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<QuestionDto> getAllQuestions() {
        // 모든 문제 조회
        List<Question> questions = questionRepository.findAll();

        return questions.stream().map(q -> new QuestionDto(
                q.getIdx(), q.getTitle(), q.getContents(),
                q.getOpt1(),q.getOpt2(),q.getOpt3(),
                q.getExplain(),q.getType()
        )).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean checkAnswerAndUpdateStats(String userId, QuestionAnswerDto requestDto) {

        Users user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Question question = questionRepository.findById(requestDto.getQuestionIdx())
                .orElseThrow(() -> new RuntimeException("문제를 찾을 수 없습니다."));

        String correctAnswer = question.getMzAnswer();
        boolean isCorrect = correctAnswer.equals(requestDto.getAnswerNumber());

        QueAnswer answerRecord = new QueAnswer();
        answerRecord.setQuestion(question);
        answerRecord.setUser(user);
        answerRecord.setAnswerNumber(requestDto.getAnswerNumber());
        queAnswerRepository.save(answerRecord);

        String isMz = requestDto.getIsMz();

        if ("Y".equalsIgnoreCase(isMz)) {
            question.setMzSolvedCount(question.getMzSolvedCount() + 1);
            if (isCorrect) {
                question.setMzCorrectCount(question.getMzCorrectCount() + 1);
            }
        } else {
            question.setOldSolvedCount(question.getOldSolvedCount() + 1);
            if (isCorrect) {
                question.setOldCorrectCount(question.getOldCorrectCount() + 1);
            }
        }

        questionRepository.save(question);

        return isCorrect;
    }
}