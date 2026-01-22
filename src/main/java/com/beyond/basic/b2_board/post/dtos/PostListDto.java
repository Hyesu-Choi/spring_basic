package com.beyond.basic.b2_board.post.dtos;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
// post 리스트 조회시 응답값들
public class PostListDto {
    private Long id;
    private String title;
    private String category;
    private String authorEmail;

//    post 리스트 응답위해서 엔티티에서 dto로 바꾸는 빌더
    public static PostListDto fromEntity(Post post) {
        return PostListDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .category(post.getCategory())
                .authorEmail(post.getAuthor().getEmail())
                .build();
    }
}
