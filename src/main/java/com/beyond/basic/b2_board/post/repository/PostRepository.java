package com.beyond.basic.b2_board.post.repository;

import com.beyond.basic.b2_board.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
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
    @Query("select p from Post p inner join p.author")
    //테이블명에 alias(별청) 사용. post도메인에 author필드랑 조인하겠다는거임.쿼리많이나감(authorid만큼 찾는쿼리 + post조회쿼리)
    List<Post> findAllInnerJoin();  // 메서드명을 jpa 네이밍룰 안따름. 그냥 inner join으로만 쿼리짬

    //    jpql을 활용한 inner join(fetch) : N+1문제 해결D
//    순수raw : select p from post p inner join author a on a.id=p.author_id;
    @Query("select p from Post p inner join fetch p.author")
    // inner join fetch로 짬. 쿼리 1번만 나감. N+1해결.
    List<Post> findAllFetchInnerJoin();

//    Page객체 안에는 content(List<Post>), totalPages, totalElement 등의 정보 포함
//    Page<Post> findAllByDelYn (Pageable pageable, String delYn);

    //    검색 + 페이징처리까지 할 경우, 아래와 같이 매개변수 선언. (Specification, Pageable 순서 - SimpleJpaRepository에서 정의)
//    @EntityGraph(attributePaths = "author") -> fetch join 어노테이션 . author로 fetch join 나간거랑 똑같은 효과의 어노테이션(N+1해결위해 사용)
    Page<Post> findAll(Specification<Post> specification, Pageable pageable);  // 매개변수 순서 바뀌면 안됨.

//    예약 글쓰기를 위한 findAllByAppointment 별도추가
    List<Post> findAllByAppointment(String appointment);

}
