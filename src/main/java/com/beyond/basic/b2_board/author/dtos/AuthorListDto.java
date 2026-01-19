package com.beyond.basic.b2_board.author.dtos;

import com.beyond.basic.b2_board.author.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
// 회원리스트 조회에 리턴할값들(비밀번호는 개인정보 이슈로 인해서 리턴하면 안됨)
public class AuthorListDto {
    private Long id;
    private String name;
    private String email;

    public static AuthorListDto fromEntity(Author author) {
        return AuthorListDto.builder()
                .id(author.getId())
                .name(author.getName())
                .email(author.getEmail())
                .build();
    }
}
