package com.beyond.basic.b2_board.author.domain;

import jakarta.persistence.*;
import lombok.*;
// 1:1 관계 예시
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String street;
    private String zipCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", unique = true, foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT), nullable = false)
    private Author author;


}
