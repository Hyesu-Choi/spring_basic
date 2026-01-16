package com.beyond.basic.b2_board.author.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Author {
    private Long id;
    private String name;
    private String email;
    private String password;

}
