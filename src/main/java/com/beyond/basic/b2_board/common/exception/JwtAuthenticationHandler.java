package com.beyond.basic.b2_board.common.exception;

import com.beyond.basic.b2_board.common.dtos.CommonErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

//이 예외 코드를 filterchain에 에러 잡아내는 목적으로 사용
@Component
public class JwtAuthenticationHandler implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Autowired
    public JwtAuthenticationHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        start line + header 조립
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401 상태코드 세팅
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        CommonErrorDto dto = CommonErrorDto.builder().status_code(401).error_message("token이 없거나 유효하지 않습니다.").build();

        ObjectMapper objectMapper = new ObjectMapper();
        String data = objectMapper.writeValueAsString(dto);
        PrintWriter printWriter = response.getWriter();
        printWriter.write(data);
        printWriter.flush();

    }
}

