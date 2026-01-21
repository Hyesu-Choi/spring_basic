package com.beyond.basic.b2_board.author.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorJpaRepository {

    //    EntityManager는 jpa의 핵심인터페이스
    private final EntityManager entityManager;

    @Autowired
    public AuthorJpaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Author author) {
//        persist : 순수jpa에서 데이터를 insert 해주는 메서드
        entityManager.persist(author);
    }

    public Optional<Author> findById(Long id) {
//        find : 순수jpa에서 pk로 데이터를 select 하는 메서드
        Author author = entityManager.find(Author.class, id);
        return Optional.ofNullable(author);
    }

    public List<Author> findAll() {
//        순수jpa에서는 제한된 메서드의 제공으로, jpql을 사용하여 직접쿼리 작성하는 경우가 있음.
//        jpql : 일반문자열 형식의 raw 쿼리가 아닌 객체지향쿼리문
//        jpql의 문법 : db의 테이블명/컬럼명을 대상으로 조회하는 것이 아니라, 엔티티명/필드명을 기준으로 사용. 별칭 사용 가능. (테이블 기준 아니라 객체 기준으로 작성,* 대신 별칭 사용)
        List<Author> authorList = entityManager.createQuery("select a from Author a", Author.class).getResultList();
        return authorList;
    }

    public Optional<Author> findByEmail(String email) {
        List<Author> authorList = entityManager
                .createQuery("select a from Author a where a.email = :email", Author.class)
                .setParameter("email", email)
                .getResultList();
        Author author = null;
        if (authorList.size() != 0) {
            author = authorList.get(0);
        }
        return Optional.ofNullable(author);
    }

    public void delete(Long id) {
//        remove : 순수 jpa에서 제공되는 삭제 메서드
        Author author = entityManager.find(Author.class, id);
        entityManager.remove(author);

    }

}
