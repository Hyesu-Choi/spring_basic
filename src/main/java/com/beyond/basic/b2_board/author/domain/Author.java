package com.beyond.basic.b2_board.author.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @ToString  // 엔티티는 안정성이 중요해서 setter 빼야함
//Builder 패턴은 AllArgs 생성자 기반으로 동작
//테이블명과 컬럼명과 매핑된 객체
@Builder
public class Author {
    private Long id;
    private String name;
    private String email;
    private String password;

}
