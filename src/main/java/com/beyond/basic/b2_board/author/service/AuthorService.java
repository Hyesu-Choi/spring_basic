package com.beyond.basic.b2_board.author.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dtos.AuthorCreateDto;
import com.beyond.basic.b2_board.author.dtos.AuthorDetailDto;
import com.beyond.basic.b2_board.author.dtos.AuthorListDto;
import com.beyond.basic.b2_board.author.repository.AuthorJdbcRepository;
import com.beyond.basic.b2_board.author.repository.AuthorMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

// @Component 어노테이션을 통해 싱글톤(단 하나의) 객체가 생성되고, 스프링에 의해 스프링컨텍스트에서 관리
@Service
// 반드시 초기화 되어야 하는 필드(final 변수 등)를 대상으로 생성자를 자동 생성.
//@RequiredArgsConstructor  // 방법3.의존성주입시 필요한 어노테이션
public class AuthorService {
//        의존성주입(DI) 방법 1. 필드 주입 : Autowired 어노테이션 사용(간편방식)
//    @Autowired
//    private AuthorRepository authorRepository;

    //        의존성주입(DI) 방법 2. 생성자주입방식(가장 많이 사용되는 방식)
//    장점1) final을 통해 상수로 사용 가능(안정성 향상)
//    장점2) 다형성 구현 가능(interface 사용가능)?좀 더 공부해보기
//    장점3) 순환참조 방지(컴파일타임에 에러 check)
    private final AuthorJdbcRepository authorMemoryRepository;  //  이 자리에 인터페이스 사용 가능
    //    생성자가 하나밖에 없을때에는 @Autowired 생략 가능.
    @Autowired  // 그냥 무조건 붕이는게 좋음.
    public AuthorService(AuthorJdbcRepository authorMemoryRepository) { // 매개변수로 주입의 객체 대상을 정확히 지정해서 넣어줌.
        this.authorMemoryRepository = authorMemoryRepository;
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
        Author author = dto.toEntity();
        authorMemoryRepository.save(author);

    }

    public AuthorDetailDto findById(Long id) {
        Optional<Author> optAuthor = authorMemoryRepository.findById(id);
        Author author = optAuthor.orElseThrow(() -> new NoSuchElementException("entity is not found"));
//        dto 조립
//        fromEntity는 아직 dto 객체가 만들어지지 않은 상태이므로 static 메서드로 설계 ?
        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);
        return dto;
    }

    //    회원정보 전체 조회 로직
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

        return authorMemoryRepository.findAll().stream().map(a -> AuthorListDto.fromEntity(a)).collect(Collectors.toList());

    }

}
