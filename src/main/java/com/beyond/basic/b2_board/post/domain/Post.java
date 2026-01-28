package com.beyond.basic.b2_board.post.domain;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


//-게시글 : id, title(not null), contents(3000자이하), category, authorEmail(not null), delYn(String)-fromEntity, toEntity 활용
@AllArgsConstructor
@NoArgsConstructor
@Getter @ToString
@Builder
@Entity
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(length = 3000)
    private String contents;
    private String category;
//    @Column(nullable = false)
//    private Long authorId;

//    ManyToOne을 통해 FK 설정(author_id컬럼)
//    ManyToOne을 통해 author_id 컬럼으로 author객체 조회 및 객체 자동 생성.
//    fetch lazy(지연로딩) : author객체를 사용하지 않는 한, author 객체 생성 X(서버부하감소)   // Eager: default. left join. Lazy : 사용할때만 post조회, author조회 해오겠댜
    @ManyToOne(fetch = FetchType.LAZY)  // fetch:데이터를 가져오겠다. 무조건 조회할껀지(Eager:즉시조회,Lazy:지연로딩). post 입장에서는 n:1, author 입장에서는 1:n. fk 자동설정.
//    ManyToOne 어노테이션만 추가하더라도, 아래 옵션이 생략되어 있는 것. fk르 설정하지 않고자 할 때, NO_CONSTRAINT 설정
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT), nullable = false) //이미 내장되어있어서 안써도 되긴하는데, 직관적으로 보이기 위해서 작성해두는게 좋음. 컬럼명 author_id로 fk 만들게따
    private Author author;  //db에 객체 저장 못해서, jpa에서 알아서 author의 pk인 id만 꺼내서 author_id로 db에 저장해줌

//    delYN default 추가하기 위한 옵션
    @Builder.Default  // 빌더 어노테이션 붕어있어야 사용 가능.
    private String delYn = "N";

    @Builder.Default
    private String appointment = "N";
    @Builder.Default
    private LocalDateTime appointmentTime = LocalDateTime.now();



    public void updateDelYn(String delYn) {
        this.delYn = delYn;
    }

    public void deletePost(){
        this.delYn = "Y";
    }
//    영속성 컨텍스트에 변경사항 올리고 정상완료되면 자동 save해줌.
    public void updateAppointment(String appointment) {
        this.appointment = appointment;
    }
}
