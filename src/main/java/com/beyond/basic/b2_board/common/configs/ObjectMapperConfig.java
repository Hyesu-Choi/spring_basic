package com.beyond.basic.b2_board.common.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//ObjectMapper : JSON 컨텐츠를 Java 객체로 deserialization 하거나 Java 객체를 JSON으로 serialization 할 때 사용하는 Jackson 라이브러리의 클래스
//별도로 커스텀해서 사용할 수도 있음
@Configuration
public class ObjectMapperConfig {
//    1. ObjectMapper 객체는 스프링에서 기본적으로 싱글톤객체로 생성
//    2. 해당 객체는 모든 Controller 등에서 사용되고 있고, 아래와 같이 별도로 커스텀이 가능
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }
}
