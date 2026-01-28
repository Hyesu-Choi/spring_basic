package com.beyond.basic.b3_servlet;

import com.beyond.basic.b1_basic.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

// 서블릿(servlet)은 사용자의 http 요청을 쉽게 처리하고, 사용자에게 http 응답을 쉽게 조립해주는 기술
// 서블릿에서는 url매핑을 메서드단위가 아닌, 클래스 단위로 지정
@WebServlet("/servlet/get")
public class ServletReqGet extends HttpServlet {

    private final ObjectMapper objectMapper;

    public ServletReqGet(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

//    HttpServlet의 doGet 오버라이딩 해야함
    @Override  // 상위 클래스 메서드 재정의
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//       사용자의 요청 : http://localhost:8080/servlet/get?id=1
//       사용자에게 줄 응답 : json객체형식
        String id = req.getParameter("id");
        System.out.println(id);

        Member member = new Member("hong1", "hong1@naver.com");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

//        objectmapper로 조립
        String data = objectMapper.writeValueAsString(member);

        PrintWriter printWriter = resp.getWriter();
        printWriter.print(data);
        printWriter.flush();
    }
}
