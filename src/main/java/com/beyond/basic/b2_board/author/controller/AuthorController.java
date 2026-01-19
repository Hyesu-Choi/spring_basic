package com.beyond.basic.b2_board.author.controller;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dtos.AuthorCreateDto;
import com.beyond.basic.b2_board.author.dtos.AuthorDetailDto;
import com.beyond.basic.b2_board.author.dtos.AuthorListDto;
import com.beyond.basic.b2_board.author.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController  // @Controller + 응답값에서 @ResponseBody 생략가능. 여긴 무조건 @RestController써야함. 고유의 기능(url 라우팅 수행) 수행해야하므로
@RequestMapping("/author")
public class AuthorController {

    //    service 의존성 주입.. 의존성 주입 두번 하면 안됨..스프링에서 해당 빈 못 찾아옴.
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    //    회원가입
    @PostMapping("/create")
    public String authorCreate(@RequestBody AuthorCreateDto dto) {
        authorService.save(dto); // 사용자에게 입력받은값을 service로 넘김
        return "ok";
    }

    //    회원목록조회
    @GetMapping("/list")
    public List<AuthorListDto> findAll() {
        List<AuthorListDto> dtoList = authorService.findAll();
        return dtoList;
    }

    //    회원상세조회
    @GetMapping("/{id}")
    public AuthorDetailDto findById(@PathVariable Long id) {
        AuthorDetailDto dto = authorService.findById(id);
        return dto;
    }

    //    회원탈퇴
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        System.out.println(id);
        return "ok";
    }


}
