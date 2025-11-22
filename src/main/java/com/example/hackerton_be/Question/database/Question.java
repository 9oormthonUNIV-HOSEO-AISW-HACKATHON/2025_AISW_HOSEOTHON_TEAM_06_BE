package com.example.hackerton_be.Question.database;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "que_idx", nullable = false)
    private Long idx;

    @Column(name = "que_title", length = 100)
    private String title;

    @Column(name = "que_contents", length = 255, nullable = false)
    private String contents;

    @Column(name = "que_answer", length = 1, nullable = false)
    private String mzAnswer;

    @Column(name = "que_mz_most_answer", length = 1)
    private String mzMostAnswer;

    @Column(name = "que_old_most_answer", length = 1)
    private String oldMostAnswer;

    @Column(name = "que_opt1", length = 100, nullable = false)
    private String opt1;

    @Column(name = "que_opt2", length = 100, nullable = false)
    private String opt2;

    @Column(name = "que_opt3", length = 100, nullable = false)
    private String opt3;

    @Column(name = "que_explain", length = 255, nullable = false)
    private String explain;

    @Column(name = "que_type", length = 1, nullable = false)
    private String type; // S 기술, C 상황

    @Column(name = "que_mz_solved_nt", nullable = false)
    private Integer mzSolvedCount;

    @Column(name = "que_mz_answer_nt", nullable = false)
    private Integer mzCorrectCount;

    @Column(name = "que_old_solved_nt", nullable = false)
    private Integer oldSolvedCount;

    @Column(name = "que_old_answer_nt", nullable = false)
    private Integer oldCorrectCount;
}