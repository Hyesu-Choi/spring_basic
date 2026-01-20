package com.beyond.basic.b2_board.author.controller;

import com.beyond.basic.b2_board.author.dtos.AuthorDetailDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// ResponseEntity : http 응답 객체에 body 뿐만 아니라 상태코드 및 header 요소를 바꿔야 하는 경우에 사용
@RestController
@RequestMapping("/response_entity")
public class ResponseEntityController {
    //    @ResponseStatus 어노테이션 사용
//    단점 : 상황에 따른 분기처리의 어려움
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("/annotation")
    public String annotation() {
        return "ok";
    }

    //    ResponseEntity 방식 : 분기처리 불가능함.
    @GetMapping("/method1")
    public ResponseEntity<String> method1() {
        return new ResponseEntity<>("OK", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/method2")
//    ? : Ojbect와 비슷한 기능함.
    public ResponseEntity<?> method2() {
        return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    //    가장 많이 사용되는 방식
//    ResponseEntity, ?(any), 빌더패턴을 사용하여 상태코드, header, body를 쉽게 생성하고 있다
    @GetMapping("/method3")
    public ResponseEntity<?> method3() {
        AuthorDetailDto dto = AuthorDetailDto.builder()
                .id(1L).name("홍길동").email("hong@naver.com").password("1234").build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
//                .header("Content-Type", "application/json")
                .body(dto);  // builder, build생략되어있음
    }

    @GetMapping("/method4")
    public ResponseEntity<?> method4() {
        AuthorDetailDto dto = AuthorDetailDto.builder()
                .id(1L).name("홍길동").email("hong@naver.com").password("1234").build();
        return ResponseEntity.ok(dto);

    }


}
