package com.beyond.basic.b2_board.author.domain;

import com.beyond.basic.b2_board.common.domain.BaseTimeEntity;
import com.beyond.basic.b2_board.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString  // 엔티티는 안정성이 중요해서 setter 빼야함
//Builder 패턴은 AllArgs 생성자 기반으로 동작
//테이블명과 컬럼명과 매핑된 객체
@Builder
// JPA에게 Entity 관리를 위임하기 위한 어노테이션
@Entity
public class Author extends BaseTimeEntity {
    @Id  // pk 설정
//    identity : auto_increment설정. auto : id 생성전략을 jpa에게 자동설정하도록 위임.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long -> bigint, String -> varchar
    private Long id;
    //    변수명이 컬럼명으로 그대로 생성. camel case는 언더스코어로 변경. 예)nickName -> nick_name
    private String name;
    //    길이(varchar50 - 별다른 설정 없으면 varchar255), 제약조건(unique, not null) 설정
    @Column(length = 50, unique = true, nullable = false)
    private String email;
    //    @Column(name = "pw") : 컬럼명의 변경이 가능하나, 일반적으로는 일치시킴.
//    @Setter : 컬럼에 따로 붙일 수 있음
    private String password;

    //    Enumerated : enum타입은 내부적으로 숫자값을 가지고 있으나, 문자형태로 저장하겠다는 어노테이션
    @Enumerated(EnumType.STRING)
    @Builder.Default  // 초기값 설정해야하면 넣어야하는 어노테이션
    private Role role = Role.USER;

    //    일반적으로 OneToMany는 선택사항. ManyToOne은 필수사항. many쪽 데이터 조회해 올 일 없으면 굳이 설정 안해도됨.
//    mappedBy : ManyToOne 쪽의 필드명을 문자열로 지정. -> 조회해야할 컬럼을 명시. 주인
//    연관관계(fk)의 주인 설정 -> 연관관계의 주인은 author 변수를 가지고 있는 Post에 있음을 명시. fk설정을 어디서 하는지.
//    orphanRemoval : 자식의 자식까지 연쇄적으로 삭제해야할 경우 모든 부모에 orphanRemoval=true 옵션 추가. // all=persist+remove
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
     private List<Post> postList = new ArrayList<>(); // persist. 초기화 필요
//    List<Post> postList;  //remove

//    @OneToOne(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Address adress;

    private String profileImageUrl;

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

}
