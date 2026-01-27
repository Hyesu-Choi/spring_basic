package com.beyond.basic.b2_board.post.repository;

import com.beyond.basic.b2_board.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByDelYn(String delYn);  // 삭제여부 Yes, No인지에 따라 해당하는 데이터 리스트로 리턴
//    List<Post> findAllByAuthorIdAndDelYn(Long authorId, String delYn);

//    jpql을 활용한 일반 inner join : N+1 문제 해결X
//    jpql과 raw 쿼리의 차이
//      1. jpql을 사용한 inner join시, 별도의 on조건 필요X
//      2. jpql은 컴파일타임에 에러를 check
//    순수raw : select p.* from post p inner join author a on a.id=p.author_id;
    @Query("select p from Post p inner join p.author")  //테이블명에 alias(별청) 사용. post도메인에 author필드랑 조인하겠다는거임.쿼리많이나감(authorid만큼 찾는쿼리 + post조회쿼리)
    List<Post> findAllInnerJoin();  // 메서드명을 jpa 네이밍룰 안따름. 걍 아무거나 지어도됨.
//    jpql을 활용한 inner join(fetch) : N+1문제 해결D
//    순수raw : select p from post p inner join author a on a.id=p.author_id;
    @Query("select p from Post p inner join fetch p.author")  //쿼리 1번만 나감
    List<Post> findAllFetchInnerJoin();

}
