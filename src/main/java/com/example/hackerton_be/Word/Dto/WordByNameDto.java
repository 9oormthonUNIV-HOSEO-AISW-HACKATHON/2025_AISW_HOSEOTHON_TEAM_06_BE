package com.example.hackerton_be.Word.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WordByNameDto {
    private Long idx;
    private String name;
    private String mean;
    private String categories;
}
