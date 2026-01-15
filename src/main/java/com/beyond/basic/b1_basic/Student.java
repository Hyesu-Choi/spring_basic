package com.beyond.basic.b1_basic;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Student {
    private String name;
    private String email;
    private List<Score> scores;

//    학생의 점수이기 때문에 내부 클래스로 선언하는게 좋다함.
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Score {
        private String subject;
        private int point;
    }
}

