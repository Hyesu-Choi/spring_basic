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
//    내부에 static 반드시 필요
//    객체마다 class가 붙는게 말이 안된다.
//    만약에 static 을 뺴고 싶다면 Student 밖에 선언해야한다.
    public static class Score {
        private String subject;
        private int point;
    }
}

