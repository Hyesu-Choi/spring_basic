package com.beyond.basic.b2_board.author.controller;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dtos.AuthorCreateDto;
import com.beyond.basic.b2_board.author.dtos.AuthorDetailDto;
import com.beyond.basic.b2_board.author.dtos.AuthorListDto;
import com.beyond.basic.b2_board.author.service.AuthorService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController  // @Controller + @ResponseBody 생략가능
@RequestMapping("/author")
public class AuthorController {
    private AuthorService authorService;
    public AuthorController() {
        this.authorService = new AuthorService();
    }
    //    회원가입
    @PostMapping("/create")
    public String authorCreate(@RequestBody AuthorCreateDto dto) {
        authorService.save(dto); // 서비스로직에 응답값 보내기
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
