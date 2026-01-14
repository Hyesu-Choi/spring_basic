package com.beyond.basic.b1_basic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter // getter자동생성
@AllArgsConstructor // 생성자 자동생성
@NoArgsConstructor // 기본생성자 자동생성
public class Member {
    private String name;
    private String email;

}
