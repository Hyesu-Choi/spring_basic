package com.beyond.basic.b2_board.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

// 컨트롤러 어노테이션이 붙어있는 모든 클래스의 예외를 아래의 클래스에서 인터셉팅(가로채기)
@RestControllerAdvice
public class CommonExceptionHandler {
//    에외 클래스 단위로 메서드 생성. 컨트롤러 계층 바로 앞에 있음.

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegal(IllegalArgumentException e) { // 에러 주입
        e.printStackTrace();
        CommonErrorDto dto = CommonErrorDto.builder()
                .status_code(400).error_message(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    //    valid 에러 캐치
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValidException(MethodArgumentNotValidException e) {
        e.printStackTrace();
        CommonErrorDto dto = CommonErrorDto.builder()
                .status_code(400)
                .error_message(e.getFieldError().getDefaultMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> noSuchElement(NoSuchElementException e) {
        e.printStackTrace();
        CommonErrorDto dto = CommonErrorDto.builder()
                .status_code(404).error_message(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
    }

//    메서드로 분기 처리 안한 나머지 에러들은 에러의 조상 클래스 exception으로 처리한댜
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exception(Exception e) {
        e.printStackTrace();
        CommonErrorDto dto = CommonErrorDto.builder()
                .status_code(500).error_message(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }


}
