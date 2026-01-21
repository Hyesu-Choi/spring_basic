package com.beyond.basic.b2_board.author.domain;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @ToString  // 엔티티는 안정성이 중요해서 setter 빼야함
//Builder 패턴은 AllArgs 생성자 기반으로 동작
//테이블명과 컬럼명과 매핑된 객체
@Builder
// JPA에게 Entity 관리를 위임하기 위한 어노테이션
@Entity
public class Author {
    @Id  // pk 설정
//    identity : auto_increment설정. auto : id 생성전략을 jpa에게 자동설정하도록 위임.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long -> bigint, String -> varchar
    private Long id;
//    변수명이 컬럼명으로 그대로 생성. camel case는 언더스코어로 변경. 예)nickName -> nick_name
    private String name;
//    길이(varchar50 - 별다른 설정 없으면 varchar255), 제약조건(unique, not null) 설정
    @Column(length = 50, unique = true, nullable = false)
    private String email;
//    @Column(name = "pw") : 컬럼명의 변경이 가능하나, 일반적으로는 일치시킴.
//    @Setter : 컬럼에 따로 붙일 수 있음
    private String password;

    public void updatePassword(String password) {
        this.password = password;
    }

}
