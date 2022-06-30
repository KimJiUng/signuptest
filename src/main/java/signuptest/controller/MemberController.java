package signuptest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import signuptest.dto.MemberDto;
import signuptest.service.MemberService;

@Controller
public class MemberController {

    @Autowired
    MemberService memberService;

    // 메인페이지 이동 매핑
    @GetMapping("/")
    public String main(){
        return "main";
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
