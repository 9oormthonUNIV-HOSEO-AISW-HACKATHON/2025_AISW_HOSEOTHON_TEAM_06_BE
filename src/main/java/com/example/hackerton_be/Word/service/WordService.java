package com.example.hackerton_be.Word.service;

import com.example.hackerton_be.Word.Dto.WordByNameDto;

import java.util.List;

public interface WordService {

    List<WordByNameDto> searchWordByName(String name);

    List<WordByNameDto> getTop3Words();
}
