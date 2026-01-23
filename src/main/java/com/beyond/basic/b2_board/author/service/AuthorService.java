package com.beyond.basic.b2_board.author.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dtos.*;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

// @Component 어노테이션을 통해 싱글톤(단 하나의) 객체가 생성되고, 스프링에 의해 스프링컨텍스트에서 관리
@Service
// 반드시 초기화 되어야 하는 필드(final 변수 등)를 대상으로 생성자를 자동 생성.
// @RequiredArgsConstructor  // 방법3.의존성주입시 필요한 어노테이션

// 스프링에서 jpa를 활용할 때 트랜잭션처리(commit, rollback) 지원.
// commit의 기준점 : 메서드 정상 종료 시점. rollback의 기준점 : 예외발생했을경우.
@Transactional
public class AuthorService {
//        의존성주입(DI) 방법 1. 필드 주입 : Autowired 어노테이션 사용(간편방식)
//    @Autowired
//    private AuthorRepository authorRepository;

    //        의존성주입(DI) 방법 2. 생성자주입방식(가장 많이 사용되는 방식)
//    장점1) final을 통해 상수로 사용 가능(안정성 향상)
//    장점2) 다형성 구현 가능(interface 사용가능)?좀 더 공부해보기
//    장점3) 순환참조 방지(컴파일타임에 에러 check)
    private final AuthorRepository authorRepository;  //  이 자리에 인터페이스 사용 가능
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    //    생성자가 하나밖에 없을때에는 @Autowired 생략 가능.
    @Autowired  // 그냥 무조건 붕이는게 좋음.
    public AuthorService(AuthorRepository authorRepository, PostRepository postRepository, PasswordEncoder passwordEncoder) {
        this.authorRepository = authorRepository; // 매개변수로 주입의 객체 대상을 정확히 지정해서 넣어줌.
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //        의존성주입(DI) 방법 3. @RequiredArgsConstructor 어노테이션 사용
//    반드시 초기화되어야 하는 필드를 선언하고, 위 어노테이션 선언시 생성자주입방식으로 의존성이 주입됨
//    단점 : 다형성 설계는 불가
//    private final AuthorRepository authorRepository;  // final 붙여야함. final 필드 대상으로 생성자 만들어줌


    //    회원가입 로직
    public void save(AuthorCreateDto dto) {

//        방법1. 객체 직접 조립
//        1-1). 생성자만을 활용한 객체 조립
//        Author author = new Author(null, dto.getName(), dto.getEmail(), dto.getPassword());
//        authorRepository.save(author); // 객체 직접 조립해서 repo에 저장
//        1-2). Builder 패턴을 활용해 객체 조립(표준의 방법)
//        장점 : 1)매개변수의 개수의 유연성 2)매개변수의 순서의 유연성
//        @Builder 쓰려면 dto에 @Builder 어노테이션 선언해줘야함.
//        Author author = Author.builder()
//                .email(dto.getEmail())
//                .name(dto.getName())
//                .password(dto.getPassword())
//                .build();

//        방법2. toEntity, fromEntity 패턴을 통한 객체 조립
//        객체조립이라는 반복적인 작업을 별도의 코드로 떼어내 공통화하는 작업. 보통은 dto에 떼냄.
        //        email중복검증
        if (authorRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        Author author = dto.toEntity(passwordEncoder.encode(dto.getPassword()));
        //        cascade persist를 활용한 예시
        author.getPostList().add(Post.builder().title("안녕하세요").author(author).build());

        Author authorDb = authorRepository.save(author);  // 회원가입하면서,
//        cascade 옵션이 아닌 예시
//        postRepository.save(Post.builder().title("안녕하세요").author(authorDb).build());  // 기본 포스트 추가.

//        예외 발생시 transactional 어노테이션에 의해 rollback 처리
//        authorRepository.findById(10L).orElseThrow(() -> new NoSuchElementException("entity is not found")); //예외강제발생
    }

    //    로그인
    public Author login(AuthorLoginDto dto) {
        Optional<Author> optAuthor = authorRepository.findByEmail(dto.getEmail());
        boolean check = true;
        if (!optAuthor.isPresent()) {  // 이메일 일치하는거 없으면
            check = false;
        } else {
            if (!passwordEncoder.matches(dto.getPassword(), optAuthor.get().getPassword())) {  // db비밀번호, 입력비밀번호 일치안하면
                check = false;
            }
        }

        if (!check) {
            throw new IllegalArgumentException("email 또는 비밀번호가 일치하지 않습니다.");
        }
        return optAuthor.get();


    }

    //    회원상세조회
//    트랜잭션 처리가 필요없는 조회만 있는 메서드의 경우 성능향상을 위해 readOnly 처리
    @Transactional(readOnly = true)
    public AuthorDetailDto findById(Long id) {
        Optional<Author> optAuthor = authorRepository.findById(id);
        Author author = optAuthor.orElseThrow(() -> new NoSuchElementException("entity is not found"));
//        dto 조립
//        fromEntity는 아직 dto 객체가 만들어지지 않은 상태이므로 static 메서드로 설계
//        List<Post> postList = postRepository.findAllByAuthorIdAndDelYn(author.getId(), "No");
//        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author, 0);
        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);

        return dto;
    }

    //    회원정보 전체 조회 로직
    @Transactional(readOnly = true)
    public List<AuthorListDto> findAll() {
//        List<Author> authorList = authorRepository.findAll();
//        List<AuthorListDto> authorListDtos = new ArrayList<>();
//        for (Author a : authorList) {
//            AuthorListDto dto = new AuthorListDto(a.getId(), a.getName(), a.getEmail());
//            authorListDtos.add(dto);
//        }
//        return authorListDtos;
//        List<Author> authorList = authorRepository.findAll();
//        List<AuthorListDto> authorListDtos = new ArrayList<>();
//        for (Author a : authorList) {
//            AuthorListDto dto = AuthorListDto.fromEntity(a);
//            authorListDtos.add(dto);
//        }
//        return authorListDtos;

        return authorRepository.findAll().stream().map(a -> AuthorListDto.fromEntity(a)).collect(Collectors.toList());
    }

    public void delete(Long id) {
//        데이터 조회 후 없다면 예외처리
        Author author = authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("entity is not found"));
//        삭제작업
        authorRepository.delete(author);
    }

    public void updatePassword(AuthorUpdatePwDto dto) {
//        이메일 찾기. 없으면 EntityNotFoundException 에러 핸들러 쓰기
        Author author = authorRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new EntityNotFoundException("입력하신 이메일을 찾을 수 없습니다.")); // 가상의DB(영속성 컨텍스트) 임시보관
//        setter 필요한데, 직관적인 메서드(updatePassword) 만들어서 사용.
        author.updatePassword(dto.getPassword());  // 패스워드업데이트
//        insert, update 모두 save 메서드 사용 -> 변경감지로 대체
//        authorRepository.save(author);

//        영속성컨텍스트 : 애플리케이션과 DB 사이에서 객체를 보관하는 가상의 DB 역할
//        1) 쓰기지연 : insert, update 등의 작업사항을 즉시 실행하지 않고, 커밋시점에 모아서 실행(성능향상)
//        2) 변경감지(Dirty checking) : 영속상태(managed)의 엔티티는 트랜잭션 커밋시점에 변경감지를 통해 별도의 save 없이 DB에 반영.
    }

}
