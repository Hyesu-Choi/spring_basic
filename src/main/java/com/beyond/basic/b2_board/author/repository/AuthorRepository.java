package com.beyond.basic.b2_board.author.repository;

import com.beyond.basic.b2_board.author.domain.Author;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorRepository {
    private List<Author> authorList = new ArrayList<>(); // db라 가정함
    private static Long staticId = 1L;

    public void save(Author author) {
        this.authorList.add(author);
        author.setId(staticId++);
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
