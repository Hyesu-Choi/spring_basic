package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dtos.PostCreateDto;
import com.beyond.basic.b2_board.post.dtos.PostDetailDto;
import com.beyond.basic.b2_board.post.dtos.PostListDto;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {
    //    author객체도 필요해서 의존성 주입 추가 해줌.
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

    public void save(PostCreateDto dto) {
//        등록시 입력한 이메일과 똑같은 author정보를 객체에 담아줌
//        Author author = authorRepository.findByEmail(dto.getAuthorEmail()).orElseThrow(() -> new EntityNotFoundException("Author not found"));
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        System.out.println(email);
        Author author = authorRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Author not found"));
        postRepository.save(dto.toEntity(author));
    }

    @Transactional(readOnly = true)
    public PostDetailDto findById(Long id) {
//        찾는 포스트 아이디없으면 에러 로직 추가해야함
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("entity is not found"));
//        Author author = authorRepository.findById(post.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("author is not found"));
//        PostDetailDto dto = PostDetailDto.fromEntity(post, author);
        PostDetailDto dto = PostDetailDto.fromEntity(post);

        return dto;
    }

    @Transactional(readOnly = true)
    public List<PostListDto> findAll() { // 스트림으로 바꿔도됨
        List<Post> postList = postRepository.findAllByDelYn("No");
        List<PostListDto> postListDtoList = new ArrayList<>();
        for (Post p : postList) {
            PostListDto dto = PostListDto.fromEntity(p);
            postListDtoList.add(dto);
        }
        return postListDtoList;
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("entity is not found"));
//        delYn 설계는 entity 몇개만 하고, 나머지는 그냥 하드 delete 써야될듯.. post.delete(post)..이건 soft delete임! delYn을 Yes로 바꿨댜
        post.deletePost();
    }

}
