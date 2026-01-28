package com.beyond.basic.b2_board.post.controller;

import com.beyond.basic.b2_board.post.dtos.PostCreateDto;
import com.beyond.basic.b2_board.post.dtos.PostDetailDto;
import com.beyond.basic.b2_board.post.dtos.PostListDto;
import com.beyond.basic.b2_board.post.dtos.PostSearchDto;
import com.beyond.basic.b2_board.post.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class PostController {
    private final PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/post/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void postCreate(@RequestBody PostCreateDto dto) {
        postService.save(dto);
    }

    @GetMapping("/post/{id}")
    public PostDetailDto findById(@PathVariable Long id) {
        return postService.findById(id);
    }

    @GetMapping("/posts")
//    페이징 처리를 위한 데이터 요청 형식 : localhost:8080/posts?page=3&size=3&sort=title,asc
//    검색 + 페이징 처리를 위한 데이터 요청 형식 : localhost:8080/posts?page=3&size=3&sort=title,asc&title=hello&category=경제
    public Page<PostListDto> findAll(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, @ModelAttribute PostSearchDto searchDto) {  // requestParam, modelAttribute 기능.
        log.info("dto : {}", searchDto);
        return postService.findAll(pageable, searchDto);

    }

    @DeleteMapping("/post/{id}")
    public void delete(@PathVariable Long id) {
        postService.delete(id);
    }


}
