package com.beyond.basic.b2_board.author.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
// mybatis 기술을 사용한 레파지토리를 만들 때 필요한 어노테이션
@Mapper
public interface AuthorMybatisRepository {
    void save(Author author);
    Optional<Author> findById(Long id);
    Optional<Author> findByEmail(String email);
    List<Author> findAll();
    void delete(Long id);

}
