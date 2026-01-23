package com.beyond.basic.b2_board.author.dtos;

import com.beyond.basic.b2_board.author.domain.Author;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
//    NotEmpty :  비어있으면 안됨을 의미하는 어노테이션
//    NotBlank : " " 공백까지 포함해서 검증하는 어노테이션
    @NotBlank(message = "이름이 비어있으면 안됩니다.")
    private String name;
    @NotBlank(message = "이메일이 비어있으면 안됩니다.")
    private String email;
    @NotBlank(message = "패스퉈드는 필수값 입니다.")
    @Size(min = 8, message = "패스워드 길이는 최소 8글자 입니다.")
    private String password;

    //    dto -> entity
    public Author toEntity(String encodedPassword) {
        return Author.builder()
                .name(this.name)
                .email(this.email)
                .password(encodedPassword)
                .build();
    }

}
