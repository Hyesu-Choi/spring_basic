package com.beyond.basic.b1_basic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// Controller 어노테이션을 통해 스프링에 의해 객체가 생성되고, 관리되어 개발자가 직접 객체를 생설할 필요 없음.
// Controller 어노테이션과 url 매핑을 통해 사용자의 요청이 매서드로 분기처리
@Controller
@RequestMapping("/member")  // /@RequestMapping : /member로 시작하는 url 요청은 이 클래스로 들어옴.
public class MemberController {
    //    🌱 GET요청 리턴의 case : text,  json, html(mvc)
//    case1. 서버가 사용자에게 text데이터 return
    @GetMapping("")
//    @Controller+@responseBody=@RestController(@Controller대신 쓰고,@ResponseBody생략 가능)
//    data(text, json)를 리턴할때는 @ResponseBody 사용. 화면(html)을 리턴할때에는 @ResponseBody 생략.
    @ResponseBody
    public String textDataReturn() {
        return "honggildong1";
    }

    //    case2. 서버가 사용자에게 json형식의 문자데이터 return
    @GetMapping("/json")
    @ResponseBody
    public Member jsonDataReturn() {
        Member m1 = new Member("h1", "h1@naver.com");
//        직접 json을 직렬화 할 필요 없이 return 타입에 객체가 있다면 자동으로 직렬화
//        ObjectMapper o1 = new ObjectMapper();
//        String data = o1.writeValueAsString(m1);
        return m1;
    }

    //    case3. 서버가 사용자에게 html을 return
//    case3-1). 정적인 html return
//    @ResponseBody가 없고 retrun 타입이 String인 경우, 스프링(서버)은 templates 폴더 밑에 simple_html.html을 찾아서 return.
//    타입리프 의존성이 필요.
    @GetMapping("/html")
    public String htmlReturn() {
        return "simple_html";
    }

    //    case3-2). 서버에서 화면+데이터를 함께 주는 동적인 html return
//    현재 이 방식은 ssr(서버사이드렌더링) 방식. csr 방식은 화면은 별도제공하고, 서버는 데이터만 제공.
    @GetMapping("/html/dynamic")
    public String dynamicHtmlReturn(Model model) {
//        model객체는 데이터를 화면에 전달해주는 역할 수행.
//        name=honggildong 형태로 화면에 전달
        model.addAttribute("name", "honggildong"); //name에 홍길동 담아서 dynamic_html에 값을 넘겨줌.Model이라는 객체가 그 작업을 수행함.
        model.addAttribute("email", "honggildong@naver.com");
        return "dynamic_html";
    }


    //    🌱 GET요청의 url의 데이터 추출방식 : pathVariable, 쿼리파라미터
//    case1. pathVariable 방식을 통해 사용자로부터 url에서 데이터 추출
//    데이터의 형식 : /member/path/1
    @GetMapping("/path/{inputId}")
    @ResponseBody
    public String path(@PathVariable Long inputId) {  // 참조형 변수로 선언하면 값이 안들어오면 null이 세팅됨.
//        별도의 형 변환 없이도 원하는 자료형으로 형 변환 되어, 매개변수로 주입.(매개변수의 변수명이 url의 변수명과 일치해야함)
        System.out.println(inputId);
        return "ok";
    }

    //    case2. 쿼리파라미터 방식을 통한 url에서의 데이터 추출(주로, 검색/정렬 등의 요청 상황에서 사용)
//    case2-1). 1개의 파라미터에서 데이터 추출
//    데이터 형식 : /member/param1?name=honggildong
    @GetMapping("/param1")
    @ResponseBody
    public String param1(@RequestParam(value = "name") String inputName) {
        System.out.println(inputName);
        return "ok";
    }

    //    case2-2). 2개의 파라미터에서 데이터 추출
//    데이터 형식 : /member/param2?name=honggildong&email=hong@naver.com
    @GetMapping("/param2")
    @ResponseBody
    public String param2(@RequestParam(value = "name") String inputName, @RequestParam(value = "email") String inputEmail) {
        System.out.println(inputName);
        System.out.println(inputEmail);
        return "ok";
    }

    //    case2-3). 파라미터의 개수가 많아질 경우, ModelAttribute를 통한 데이터 바인딩
//    데이터 바인딩은 param의 데이터를 모아 객체로 자동 매핑 및 생성
//    데이터 형식 : /member/param3?name=honggildong&email=hong@naver.com
    @GetMapping("/param3")
    @ResponseBody
//    @ModelAttribute는 생략 가능.
    public String param3(@ModelAttribute Member member) {
        System.out.println(member);
        return "ok";
    }


    //    🌱 POST요청 처리 case : urlencoded, multipart-formData, json
//    case1. body의 content-type이 urlencoded 형식
//    형식 : body부에 name=lungzzi&email=lungzzi@naver.com
    @PostMapping("/url-encoded")
    @ResponseBody
//    형식이 url의 파라미터방식과 동일하므로, RequestParam  또는 데이터 바인딩 가능하다
    public String urlEncoded(@ModelAttribute Member member) {
        System.out.println(member);
        return "ok";
    }

    //    case2. body의 content-type이 multipart form data 형식
    //    case2-1). 1개의 이미지만 있는 경우
//    형식 : body부에 name=lungzzi&email=lungzzi@naver.com&profileImage=binary데이터
    @PostMapping("/multipart-formdata")
    @ResponseBody
//    형식이 rul의 파라미터방식과 동일하므로, RequestParam  또는 데이터 바인딩 가능하다
    public String multipartFormData(@ModelAttribute Member member, @RequestParam(value = "profileImage") MultipartFile profileImage) {
        System.out.println(member);
        System.out.println(profileImage.getOriginalFilename());
        return "ok";
    }

    //    case2-2). 여러개의 이미지가 있는 경우
    @PostMapping("/multipart-formdata-images")
    @ResponseBody
    public String multipartFormDataImages(@ModelAttribute Member member, @RequestParam(value = "profileImages") List<MultipartFile> profileImages) {
        System.out.println(member);
        System.out.println(profileImages.size());
        return "ok";
    }

    //    case3. body의 content-type이 json
//    case3-1). 일반적인 json 데이터 처리
//    형식 : {"name": "lungzzi", "email" : "lung@naver.con"}
    @PostMapping("/json")
    @ResponseBody
//    @RequestBody : json 데이터를 객체로 parsing.
    public String json(@RequestBody Member member) {
        System.out.println(member);
        return "ok";
    }

    //    case3-2). 배열 형식의 json 데이터 처리
//    형식 : [{"name": "lungzzi", "email" : "lung@naver.con"}, {"name": "hyesu", "email" : "lung123@naver.con"}, {"name": "hong", "email" : "hong@naver.con"} ]
//    결론은 multipart-formdata 구조 안에 json을 넣는 방식
    @PostMapping("/json-list")
    @ResponseBody
    public String jsonList(@RequestBody List<Member> memberList) {
        System.out.println(memberList);
        return "ok";
    }

    //    case3-3). 중첩된 json 데이터 처리(클래스설계)
//    데이터 형식 : {"name":"hongildong", "email":"hong1@naver.com", "scores":[{"subject":"math", "point":100}, {"subject":"english", "point":90}, {"subject":"korean", "point":100}]}
    @PostMapping("/json-nested")
    @ResponseBody
    public String jsonNested(@RequestBody Student student) {
        System.out.println(student);
        return "ok";
    }

    //    case3-4). json+file 이 함께있는 데이터 처리
//    형식 : member={"name":"lungzzi", "email": "lung@naver.com"}&profileImage=바이너리
    @PostMapping("/json-file")
    @ResponseBody
//    json과 file을 함께 처리해야할 때는 일반적으로 @RequestPart 사용
    public String jsonFile(@RequestPart("member") Member member, @RequestPart("profileImage") MultipartFile profileImage) {
        System.out.println(member);
        System.out.println(profileImage.getOriginalFilename());
        return "ok";
    }

}
