package com.beyond.basic.b2_board.author.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dtos.*;
import com.beyond.basic.b2_board.author.repository.*;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

//Component어노테이션을 통해 싱글톤(단하나의)객체가 생성되고, 스프링에 의해 스프링컨텍스트에서 관리
@Service
//반드시 초기화 되어야 하는 필드(final 변수 등)를 대상으로 생성자를 자동생성
//@RequiredArgsConstructor
//스프링에서 jpa를 활용할때 트랜잭션처리(commit, 롤백)지원.
//commit기준점 : 메서드 정상 종료 시점. rollback의 기준점 : 예외발생했을경우.
@Transactional
public class AuthorService {
    //    의존성주입(DI)방법1. 필드주입 : Autowired 어노테이션사용 (간편방식)
//    @Autowired
//    private AuthorRepository authorRepository;

    //    의존성주입(DI)방법2. 생성자주입방식(가장많이 사용되는 방식)
//    장점1)final을 통해 상수로 사용 가능(안정성향상)
//    장점2)다형성 구현가능(interface사용가능)
//    장점3)순환참조방지(컴파일타임에 에러check)
    private final AuthorRepository authorRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket1}")
    private String bucket;

    //    생성자가 하나밖에 없을떄에는 Autowired생략가능
    @Autowired
    public AuthorService(AuthorRepository authorRepository, PostRepository postRepository, PasswordEncoder passwordEncoder, S3Client s3Client) {
        this.authorRepository = authorRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3Client = s3Client;
    }

    //    의존성주입방법3. RequiredArgsConstructor어노테이션 사용
//    반드시 초기화되어야 하는 필드를 선언하고, 위 어노테이션 선언시 생성자주입방식으로 의존성이 주입됨
//    단점 : 다형성 설계는 불가
    public void save(AuthorCreateDto dto, MultipartFile profileImage) {
//      방법1. 객체 직접 조립
//        1-1)생성자만을 활용한 객체 조립
//        Author author = new Author(null, dto.getName(), dto.getEmail(), dto.getPassword());
//        1-2)Builder패턴을 활용한 객체 조립
//        장점 : 1)매개변수의 개수의 유연성 2)매개변수의 순서의 유연성
//        Author author = Author.builder()
//                .email(dto.getEmail())
//                .name(dto.getName())
//                .password(dto.getPassword())
//                .build();

//        방법2. toEntity, fromEntity 패턴을 통한 객체 조립
//        객체조립이라는 반복적인 작업을 별도의 코드로 떼어내 공통화

//        email중복여부 검증
        if (authorRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 email입니다.");
        }
        Author author = dto.toEntity(passwordEncoder.encode(dto.getPassword()));
//        cascade persist를 활용한 예시
//        author.getPostList().add(Post.builder().title("안녕하세요").author(author).build());
        Author authorDb = authorRepository.save(author);
//        cascade 옵션이 아닌 예시
//        postRepository.save(Post.builder().title("안녕하세요").author(authorDb).build());
//        예외 발생시 transactional 어노테이션에 의해 rollback 처리
//        authorRepository.findById(10L).orElseThrow(()-> new NoSuchElementException("entity is not found"));


//       파일을 업로드를 위한 저장 객체 구성
        if (profileImage != null) {
            String fileName = "user-" + author.getId() + "-profileimage-" + profileImage.getOriginalFilename();
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(profileImage.getContentType()) //image/jpeg, video/mp4, ...
                    .build();
//        aws에 이미지 업로드(byte형태로)
            try {
                s3Client.putObject(request, RequestBody.fromBytes(profileImage.getBytes()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//        aws의 이미지 url 추출
            String imgUrl = s3Client.utilities().getUrl(a -> a.bucket(bucket).key(fileName)).toExternalForm();
            author.updateProfileImageUrl(imgUrl);
        }

    }

    //    트랜잭션 처리가 필요없는 조회만 있는 메서드의 경우 성능향상을 위해 readOnly처리
    @Transactional(readOnly = true)
    public AuthorDetailDto findById(Long id) {
        Optional<Author> optAuthor = authorRepository.findById(id);
        Author author = optAuthor.orElseThrow(() -> new NoSuchElementException("entity is not found"));
//        List<Post> postList = postRepository.findAllByAuthorIdAndDelYn(author.getId(), "N");
//      dto 조립
//      fromEntity는 아직 dto객체가 만들어지지 않은 상태이므로 static메서드로 설계
//        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author, 0);
        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);
        return dto;
    }

    @Transactional(readOnly = true)
    public AuthorDetailDto myinfo(String principal) {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Optional<Author> optAuthor = authorRepository.findByEmail(email);
        Author author = optAuthor.orElseThrow(() -> new NoSuchElementException("entity is not found"));
        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);
        return dto;
    }

    @Transactional(readOnly = true)
    public List<AuthorListDto> findAll() {
//        List<Author> authorList = authorRepository.findAll();
//        List<AuthorListDto> authorListDtos = new ArrayList<>();
//        for (Author a : authorList){
//            AuthorListDto dto = new AuthorListDto(a.getId(), a.getName(), a.getEmail());
//            authorListDtos.add(dto);
//        }
//        return authorListDtos;
        return authorRepository.findAll().stream().map(a -> AuthorListDto.fromEntity(a))
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
//        데이터 조회 후 없다면 예외처리
        Author author = authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("entity is not found"));

//        삭제작업
        authorRepository.delete(author);

    }

    public Author login(AuthorLoginDto dto) {
        Optional<Author> opt_author = authorRepository.findByEmail(dto.getEmail());
        boolean check = true;
        if (!opt_author.isPresent()) {
            check = false;
        } else {
            if (!passwordEncoder.matches(dto.getPassword(), opt_author.get().getPassword())) {
                check = false;
            }
        }
        if (!check) {
            throw new IllegalArgumentException("email 또는 비밀번호가 일치하지 않습니다.");
        }
        return opt_author.get();
    }

    public void updatePw(AuthorUpdatePwDto dto) {
        Author author = authorRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new EntityNotFoundException("entity is not found"));
        author.updatePassword(dto.getPassword());

////        insert, update 모두 save메서드 사용 -> 변경감지로 대체
//        authorRepository.save(author);

//        영속성컨텍스트 : 애플리케이션과 DB사이에서 객체를 보관하는 가상의 DB 역할
//        1)쓰기지연 : insert, update 등의 작업사항을 즉시 실행하지 않고, 커밋시점에 모아서 실행(성능향상)
//        2)변경감지(dirty checking) : 영속상태(managed)의 엔티티는 트랜잭션 커밋시점에 변경감지를 통해 별도의 save없이 DB에 반영

    }
}
