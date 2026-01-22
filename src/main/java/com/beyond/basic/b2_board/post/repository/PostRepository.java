package com.beyond.basic.b2_board.post.repository;

import com.beyond.basic.b2_board.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByDelYn(String delYn);  // 삭제여부 Yes, No인지에 따라 해당하는 데이터 리스트로 리턴
//    List<Post> findAllByAuthorIdAndDelYn(Long authorId, String delYn);

}
