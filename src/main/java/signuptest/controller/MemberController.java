package signuptest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import signuptest.dto.MemberDto;
import signuptest.service.MemberService;

@Controller
public class MemberController {

    @Autowired // 자동 빈(메모리) 생성
    MemberService memberService;

    // 메인페이지 이동 매핑
    @GetMapping("/")
    public String main(){
        return "main";
    }

    // 회원 아이디 호출하는 페이지 매핑
    @GetMapping("/info")
    @ResponseBody   // 자바객체를 HTTP요청의 바디내용으로 매핑하여 클라이언트로 전송
    public String memberinfo(){
        return memberService.현재인증된회원아이디호출();
    }

    // 회원가입 페이지 이동 매핑
    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

    // 로그인 페이지 이동 매핑
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    // 회원가입 처리 매핑
    @PostMapping("/signupcontroller")
    public String signup(MemberDto memberDto){
        memberService.signup(memberDto);    // memberService의 signup 메소드 실행
        return "main";
    }


}
