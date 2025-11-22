package com.example.hackerton_be.Word.controller;

import com.example.hackerton_be.Word.Dto.WordByNameDto;
import com.example.hackerton_be.Word.service.WordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name="단어", description = "단어 관련 API 명세서입니다.")
@RestController
@RequestMapping("/api/word")
@RequiredArgsConstructor
public class WordController {
    private final WordService wordService;

    /**
     * 단어 이름 기반 검색 API
     */
    @GetMapping("/search")
    public ResponseEntity<List<WordByNameDto>> searchWord(
            @RequestParam String name
    ) {
        List<WordByNameDto> results = wordService.searchWordByName(name);

        if (results.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        return ResponseEntity.ok(results);
    }



}
