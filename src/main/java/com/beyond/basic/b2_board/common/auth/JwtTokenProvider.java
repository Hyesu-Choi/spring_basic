package com.beyond.basic.b2_board.common.auth;

import com.beyond.basic.b2_board.author.domain.Author;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    //        중요 정보의 경우 application.yml에 저장하고, value를 통해 주입 받음.
    @Value("${jwt.secretKey}")
    private String st_secret_key;

    @Value("${jwt.expiration}")
    private int expiration;

    //    인코딩된 문자열 -> 디코딩 된 분자열 -> HS512알고리즘으로 암호화
    private Key secret_key;

    //    생성자 호출 이후에 아래 메서드를 실행하게 함으로서, @Value 시점보다 늦게 실행되게하여 값 주입의 문제 해결
    @PostConstruct
    public void init() {
        secret_key = new SecretKeySpec(Base64.getDecoder().decode(st_secret_key), SignatureAlgorithm.HS512.getJcaName());  // 디코딩+암호화
    }

//    토큰 생성
    public String createToken(Author author) {
        // sub : abc@naver.com 형태. 주된키값 setSubject로 넣기.
        Claims claims = Jwts.claims().setSubject(author.getEmail());
//        주된키값을 제외한 나머지 정보는 put을 사용하여 key:value 로 세팅
        claims.put("role", author.getRole().toString());  // role enum이라서 toString으로 명시적으로 바뀜
//        ex) claims.put("age", author.getAge()); 형태로 여러개 넣기 가능

        Date now = new Date();
//        토근의 구성요소 : 헤더, 페이로드, 시그니처(서명부)
        String token = Jwts.builder()
//                아래 3가지 요소는 페이로드
                .setClaims(claims)  // 사용자 인식정보들
                .setIssuedAt(now)  // 현재시간
                .setExpiration(new Date(now.getTime() + expiration * 60 * 1000L))  // 30분*60초*1000밀리초 : 밀리초 형태로 변환. 이건 yml에서 설정해도됨
//                secret키를 통해 서명값(signature) 생성
                .signWith(secret_key)
                .compact();
        return token;
    }
}
