package com.example.hackerton_be.Question.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerDto {
    private Long questionIdx;
    private String answerNumber;
    private String isMz;
}