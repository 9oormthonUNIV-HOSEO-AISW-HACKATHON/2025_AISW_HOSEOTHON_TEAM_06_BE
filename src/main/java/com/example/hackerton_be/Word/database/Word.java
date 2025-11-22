package com.example.hackerton_be.Word.database;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "words")
@Getter
@Setter
@NoArgsConstructor
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_idx", nullable = false)
    private Long idx;

    @Column(name = "word_name", length = 100, nullable = false)
    private String name;

    @Column(name = "word_mean", length = 255, nullable = false)
    private String mean;

    @Column(name = "word_categories", length = 2, nullable = false)
    private String categories;

    @Column(name = "word_count", nullable = false)
    private Integer count;
}