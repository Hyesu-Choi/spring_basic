package com.beyond.basic.b2_board.author.dtos;

import com.beyond.basic.b2_board.author.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
//dto 계층은 엔티티만큼의 안정성을 우선하기보다는, 편의를 위해 setter도 일반적으로 추가
@Data
@Builder
//회원가입시 사용자에게 받을 값들
public class AuthorCreateDto {
    private String name;
    private String email;
    private String password;

    //    dto -> entity
    public Author toEntity() {
        return Author.builder()
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .build();
    }

}
