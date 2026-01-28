package com.beyond.basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

// xxxApplication 이라는 파일은 프로젝트에 딱 하나만 있어야함.
// @ComponentScan에 의해 Application 파일 하위 경로의 요소들이 싱글톤객체로 Scan.
@SpringBootApplication
// 스케쥴러 사용시 필요한 어노테이션
@EnableScheduling
// 주로 web서블릿기반 구성요소(@WebServlet)을 스캔하고, 자동으로 빈으로 등록.
@ServletComponentScan
public class BasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicApplication.class, args);
    }

}
