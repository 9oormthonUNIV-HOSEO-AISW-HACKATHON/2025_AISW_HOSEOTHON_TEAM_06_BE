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
}
