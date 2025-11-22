package com.example.hackerton_be.Word.Impl;

import com.example.hackerton_be.Word.Dto.WordByNameDto;
import com.example.hackerton_be.Word.database.Word;
import com.example.hackerton_be.Word.repository.WordRepository;
import com.example.hackerton_be.Word.service.WordService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordImpl implements WordService {

    private final WordRepository wordRepository;

    @Transactional
    @Override
    public List<WordByNameDto> searchWordByName(String name) {
        List<Word> words = wordRepository.findByNameContainingIgnoreCase(name);

        for (Word word : words) {
            word.setCount(word.getCount() + 1);
        }
        return words.stream().map(word -> new WordByNameDto(
                word.getIdx(),
                word.getName(),
                word.getMean(),
                word.getCategories()
        )).collect(Collectors.toList());
    }

    /**
     * 검색 횟수(count) 기준 Top 3 단어 조회 구현
     */
    @Transactional
    @Override
    public List<WordByNameDto> getTop3Words() {
        // Repository에서 count 기준 내림차순으로 상위 3개 단어 엔티티 조회
        List<Word> topWords = wordRepository.findTop3ByOrderByCountDesc();

        // DTO로 변환하여 반환
        return topWords.stream().map(word -> new WordByNameDto(
                word.getIdx(),
                word.getName(),
                word.getMean(),
                word.getCategories()
        )).collect(Collectors.toList());
    }
}
