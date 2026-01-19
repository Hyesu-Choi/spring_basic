package com.beyond.basic.b2_board.author.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository  // 서버가 시작되는 시점에, repo 객체 만들어줌
public class AuthorMemoryRepository {
    private List<Author> authorList = new ArrayList<>(); // 메모리를 임시 db로 쓰는중.
    private static Long staticId = 1L;

    public void save(Author author) {
        this.authorList.add(author);
//        author.setId(staticId++);  // 실제 db에서는 auto Increment로 id 증가함.
    }

    public List<Author> findAll() {
        return this.authorList;
    }

    public Optional<Author> findById(Long id) {
        Author author = null;
        for (Author a : this.authorList) {
            if (a.getId().equals(id)) {
                author = a;
            }
        }
        return Optional.ofNullable(author);
    }
}
