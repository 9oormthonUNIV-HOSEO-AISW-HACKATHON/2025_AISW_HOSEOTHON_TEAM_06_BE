package com.example.hackerton_be.Question.database;

import com.example.hackerton_be.User.database.Users; // Users 엔티티는 기존 패키지 사용 가정
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "que_answer")
@Getter
@Setter
@NoArgsConstructor
public class QueAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qa_idx", nullable = false)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_que_idx", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_member_idx", nullable = false)
    private Users user;

    @Column(name = "qa_answer_num", nullable = false, length = 1)
    private String answerNumber;
}