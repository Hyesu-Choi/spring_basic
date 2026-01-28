package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dtos.PostCreateDto;
import com.beyond.basic.b2_board.post.dtos.PostDetailDto;
import com.beyond.basic.b2_board.post.dtos.PostListDto;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.dtos.PostSearchDto;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public Page<PostListDto> findAll(Pageable pageable, PostSearchDto searchDto) {
//        List<Post> postList = postRepository.findAllByDelYn("No");
//        List<Post> postList = postRepository.findAllInnerJoin();  // 그냥  join호출.n+1해결 못핰.
//        List<Post> postList = postRepository.findAllFetchInnerJoin();  //fetch조인 호출. n+1해결.

//        검색을 위한 Specification 객체 조립
        Specification<Post> specification = new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();  // 조건 분기 담아줄 리스트 생성(몇개나 담을지 몰라서 리스트로 만듬)
//                criteriaBuilder 객체 : 조건, 분기에 따른 쿼리 조립해줌
//                root : 엔티티의 컬럼명을 접근하기 위한 객체
//                criteriaBuilder : 쿼리를 생성하기 위한 객체
                predicateList.add(criteriaBuilder.equal(root.get("delYn"), "N"));  // repo에서 findByDelYn 따로 안만들고 여기서 builder로 추가하면됨.
                predicateList.add(criteriaBuilder.equal(root.get("appointment"), "N"));

//                검색 조건 개수 대로 if문 구현해야함
                if (searchDto.getTitle() != null) {
                    predicateList.add(criteriaBuilder.like(root.get("title"), "%" + searchDto.getTitle() + "%"));
                }
                if (searchDto.getCategory() != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("category"), searchDto.getCategory()));
                }
                if (searchDto.getContents() != null) {
                    predicateList.add(criteriaBuilder.like(root.get("contents"), "%" + searchDto.getContents() + "%"));
                }

                Predicate[] predicateArr = new Predicate[predicateList.size()];  // predicate조건리스트 하나씩 꺼내서 배열에 담음(배열에 담으라니까 담아야함 이유는 없음)
                for (int i = 0; i < predicateArr.length; i++) {
                    predicateArr[i] = predicateList.get(i);
                }
//                Predicate에는 조회 검색 조건들이 담길것이고, 이 Predicate list를 한 줄의 predicate 조립. and조건으로 조립함
                Predicate predicate = criteriaBuilder.and(predicateArr);
                return predicate;
            }
        };

        Page<Post> postList = postRepository.findAll(specification, pageable);

//        Page객체 안에 Entity -> Dto로 쉽게 변환할 수 있는 편의 제공
        return postList.map(p -> PostListDto.fromEntity(p));
        //응용: pageable 응답 dto 커스텀 하는거 코드 고쳐보기.. 지금은 쓸데없는 응답값 너무 많이 옴.
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("entity is not found"));
//        delYn 설계는 entity 몇개만 하고, 나머지는 그냥 하드 delete 써야될듯.. post.delete(post)..이건 soft delete임! delYn을 Yes로 바꿨댜
        post.deletePost();
    }

}
