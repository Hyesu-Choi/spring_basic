package com.beyond.basic.b2_board.author.repository;
// spring data jpa. 이게 제일 중요함

import com.beyond.basic.b2_board.author.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// SpringDataJpa를 사용하기 위해서는 JpaRepository 인터페이스를 상속하고, 상속시에 Entity 명과 Pk의 타입을 제네릭에 설정
// JpaRepository를 상속함으로서 JpaRepository의 주요기능(각종 CRUD) 상속
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
//    save, findById, findAll, delete 등은 사전에 구현

    //    그 외에 다른 컬럼으로 조회할떄에는 findBy  + 컬럼명 형식으로 선언하면 실행시점 자동구현.
    Optional<Author> findByEmail(String email);

}
