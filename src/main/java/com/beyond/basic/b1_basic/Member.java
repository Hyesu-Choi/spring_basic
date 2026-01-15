package com.beyond.basic.b1_basic;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

//@Data : @Getter + @Setter + @ToString
@Data
//@Getter // getter 자동생성
//@ToString // toString메서드 생성
@AllArgsConstructor // 생성자 자동생성
@NoArgsConstructor // 기본생성자 자동생성
public class Member {
    private String name;
    private String email;
//    MultipartFile : java/spring에서 파일처리 편의를 제공해주는 클래스
//    private MultipartFile profileImage;  // MultipartFile : 이미지 처리가 편한 클래스

}
