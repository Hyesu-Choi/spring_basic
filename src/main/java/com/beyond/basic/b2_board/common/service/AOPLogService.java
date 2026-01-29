package com.beyond.basic.b2_board.common.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

// Aspect : aop 코드임을 명시
//@Aspect
//@Component
//@Slf4j
//public class AOPLogService {
//    AOP의 대상이 되는 controller, service 등을 어노테이션 기준으로 명시
//    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")  // 이 어노테이션이 붙어있으면 aoplogservice를 실행하겠다. 입임포트 경로 붙이면됨
//    public void controllerPointCut() {}

//    AOP의 대상이 되는 controller, service 등을 패키지 구조 기준으로 명시
//    @Pointcut("within(com.beyond.basic.b2_board.author.controller.AuthorController)")
//    public void controllerPointCut() {}

//    aop 활용방법1 : around를 통해 before, jointPoint, after 코드 한꺼번에 작성.
//    @Around("controllerPointCut()")
//    joinPoint는 사용자가 실행하고자 하는 코드를 의미하고, 위에서 정의한 pointcut을 의미
//    public Object controllerLogger(ProceedingJoinPoint joinPoint) {
//        joinpoint 이전
//        log.info("aop start");
//        log.info("사용자 이메일 : " + SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());  // 이거 접근 가능한 이유는 필터 계층이 aop계층보다 더 앞에 있기 때문에 접근 가능.
//        log.info("요청 머세드명: " + joinPoint.getSignature().getName());
//
//        servlet객체에서 http요청을 꺼내는 법  // body는 일회성객체라서 body 꺼낼일 잘 없음.
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//        log.info("http url : " + request.getRequestURL());
//        log.info("http method : " + request.getMethod());
//        log.info("http 헤더 - 토큰" + request.getHeader("Authorization"));
//        log.info("http 헤더 - contentType" + request.getHeader("Content-Type"));
//
//        joinpoint 실행
//        Object object = null;
//        try {  // checked 에러 감싸서
//            object = joinPoint.proceed();
//        } catch (Throwable e) {  // unchecked 에러로 던짐.
//            throw new RuntimeException(e);
//        }
//
//        joinpoint 이후
//        log.info("aop end");
//
//        return object;
//
//    }

// aop 활용방법2. Before, After 어노테이션 사용
//    @Before("controllerPointCut()")
//    public void beforeController(JoinPoint joinPoint) {
//        log.info("aop start");
//        log.info("사용자 이메일 : " + SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
//        log.info("요청 머세드명: " + joinPoint.getSignature().getName());
//
//    }
//    @After("controllerPointCut()")
//    public void afterController(JoinPoint joinPoint) {
//        log.info("aop end");
//    }
//
//
//}
