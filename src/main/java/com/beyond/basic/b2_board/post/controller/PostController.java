package com.beyond.basic.b2_board.post.controller;

import com.beyond.basic.b2_board.post.dtos.PostCreateDto;
import com.beyond.basic.b2_board.post.dtos.PostDetailDto;
import com.beyond.basic.b2_board.post.dtos.PostListDto;
import com.beyond.basic.b2_board.post.service.PostService;
import com.beyond.basic.b2_board.common.CommonErrorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
//@RequestMapping("/post")
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
    public List<PostListDto> findAll() {
        List<PostListDto> dtoList = postService.findAll();
        return dtoList;
    }

    @DeleteMapping("/post/{id}")
    public void delete(@PathVariable Long id) {
        postService.delete(id);
    }


}
