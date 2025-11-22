package com.example.hackerton_be.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="API명세서", description = "API명세서 입니다.")
@RestController
public class SwaggerApiController {
    @Operation(summary = "01. 테스트1", description = "API명세서 test1 입니다.")
    @GetMapping("/api/test/test1")
    public ResponseEntity<String> test() {
        return ResponseEntity.status(HttpStatus.OK).body("테스트 성공적");
    }

    @Operation(summary = "02. 테스트 2", description = "API명세서 Json으로 보내기 입니다.")
    @GetMapping("/api/test/test2")
    public ResponseEntity<TestDto> test2() {
        TestDto res = new TestDto("테스터", 21);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}

@Getter
@Setter
@AllArgsConstructor // Spring이 DTO 객체를 쉽게 생성하도록 생성자 추가
class TestDto{
    private String name;
    private int age;
}