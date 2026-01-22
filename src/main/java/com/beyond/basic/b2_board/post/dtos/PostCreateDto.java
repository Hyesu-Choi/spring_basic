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
// post 등록시 받아오는 데이터
public class PostCreateDto {
    private String title;
    private String contents;
    private String category;
    private String authorEmail;

//    post 등록시 db 맞는 엔티티로 넣기 위해 재조립
    public Post toEntity(Author author) {
        return Post.builder()
                .title(this.title)
                .contents(this.contents)
                .category(this.category)
                .author(author) //post entity의 author에 넣어줌. 객체로 넣어주면 자동으로 id만 추출해서 author_id 컬럼에 넣음
//                .delYn("No") : 이게 제일 쉽긴한데, 엔티티에서 default로 까는게 나을듯
                .build();
    }


}
