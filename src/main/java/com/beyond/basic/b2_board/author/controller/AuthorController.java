package com.beyond.basic.b2_board.author.controller;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dtos.*;
import com.beyond.basic.b2_board.author.service.AuthorService;
import com.beyond.basic.b2_board.common.auth.JwtTokenProvider;
import com.beyond.basic.b2_board.common.dtos.CommonErrorDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController  // @Controller + 응답값에서 @ResponseBody 생략가능. 여긴 무조건 @RestController써야함. 고유의 기능(url 라우팅 수행) 수행해야하므로
@RequestMapping("/author")
public class AuthorController {

    //    service 의존성 주입.. 의존성 주입 두번 하면 안됨..스프링에서 해당 빈 못 찾아옴.
    private final AuthorService authorService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthorController(AuthorService authorService, JwtTokenProvider jwtTokenProvider) {
        this.authorService = authorService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //    회원가입
    @PostMapping("/create")
//    dto에 있는 validation 어노테이션과 @Valid는 한 쌍
    public ResponseEntity<?> authorCreate(@RequestBody @Valid AuthorCreateDto dto) {
//        아래의 예외처리는 ExceptionHandler에서 전역적으로 예외처리 함.
//        try{
//            authorService.save(dto);
//            return ResponseEntity.status(HttpStatus.CREATED).body("OK");
//        }
//        catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            CommonErrorDto errorDto = CommonErrorDto.builder().status_code(400).error_message(e.getMessage()).build();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
//        }

//        예외처리 공통 핸들로 만들어서, 성공 응답은 controller에서, 에러 응답은 CommonExceptionHandler에서 처리
        authorService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");

    }

    // 로그인
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String authorLogin(@RequestBody AuthorLoginDto dto) {
        Author author = authorService.login(dto);
//        토큰 생성 및 리턴
        String token = jwtTokenProvider.createToken(author);

        return token;
    }

    //    회원목록조회
    @GetMapping("/list")
//    PreAuthorize : Authentcation 객체 안의 권한정보를 확인하는 어노테이션
//    2개 이상의 Role을 허용하는 경우 :  "hasRole('ADMIN') or hasRole('SELLER')"
    @PreAuthorize("hasRole('ADMIN')")  // ROLE_ 붙어있는지 이 어노테이션이 확인함. 토큰만들때 ROLE_ 붙임. 어드민 권한 있는 사람만 조회 가능함.
    public List<AuthorListDto> findAll() {
        List<AuthorListDto> dtoList = authorService.findAll();
        return dtoList;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            AuthorDetailDto dto = authorService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            CommonErrorDto dto = CommonErrorDto.builder()
                    .status_code(404)
                    .error_message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
        }
    }
    //    아래와 같이 http 응답 body를 분기처리한다 하더라도 헤더의 상태코드는 200으로 고정되고 body만 바뀜.
//    public Object findById(@PathVariable Long id) {
//        try {
//            AuthorDetailDto dto = authorService.findById(id);
//            return dto;
//        } catch (NoSuchElementException e) {
//            e.printStackTrace();
//            return CommonErrorDto.builder().status_code(404).error_message(e.getMessage()).build();
//        }
//    }

    //    내 정보 조회
    @GetMapping("/myinfo")
//    @AuthenticationPrincipal 객체안에서 principal 꺼낼 수 있음.
    public ResponseEntity<?> myinfo(@AuthenticationPrincipal String principal) {
//        public ResponseEntity<?> myinfo() {
        System.out.println(principal);
        AuthorDetailDto dto = authorService.myInfo(principal);
        return ResponseEntity.status(HttpStatus.OK).body(dto);

    }

    //    회원탈퇴
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        authorService.delete(id);
        return "ok";
    }

    //    비밀번호 수정
    @PatchMapping("/update/password")
    public void updatePw(@RequestBody AuthorUpdatePwDto dto) {
        authorService.updatePassword(dto);
    }

}
