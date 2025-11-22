package com.example.hackerton_be.Question.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private Long idx;
    private String title;
    private String contents;
    private String opt1;
    private String opt2;
    private String opt3;
    private String explain;
    private String type; // S 또는 C
}