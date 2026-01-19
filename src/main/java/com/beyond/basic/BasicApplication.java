package com.beyond.basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// xxxApplication 이라는 파일은 프로젝트에 딱 하나만 있어야함.
// @ComponentScan에 의해 Application 파일 하위 경로의 요소들이 싱글톤객체로 Scan.
@SpringBootApplication
public class BasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicApplication.class, args);
    }

}
