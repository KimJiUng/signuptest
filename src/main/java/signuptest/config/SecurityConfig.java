package signuptest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import signuptest.service.MemberService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
                                    // 웹 시큐리티 설정 관련 상속클래스

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()    // 인증된 요청
                .antMatchers("/**").permitAll()   // 인증이 없어도 요청 가능
                .and()
                .formLogin()    // 로그인 페이지 보안 설정
                .loginPage("/login")    // 아이디, 비밀번호 입력받을 페이지 URL
                .loginProcessingUrl("/logincontroller") // 로그인 처리 할 URL
                .failureUrl("/error")   // 로그인 실패시 이동할 URL
                .defaultSuccessUrl("/") // 로그인 성공시 이동할 URL
                .usernameParameter("mid")   // 로그인시 아이디로 입력받을 변수명
                .passwordParameter("mpassword") // 로그인시 패스워드로 입력받을 변수명
                .and()
                .logout()   // 로그아웃 보안 설정
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // 로그아웃 처리할 URL
                .logoutSuccessUrl("/")  // 로그아웃 성공시 이동할 URL
                .invalidateHttpSession(true)    // 세션 초기화
                .and()
                .csrf() // 서버에게 요청할 수 있는 페이지 제한
                .ignoringAntMatchers("/logincontroller")   // 로그인 처리 URL csrf 예외처리
                .ignoringAntMatchers("/signupcontroller")   // 회원가입 처리 URL csrf 예외처리
                .and()
                .oauth2Login()// oauth2 관련 설정
                .loginPage("/login")    // oauth2 로그인 할 페이지
                .userInfoEndpoint()// 유저 정보가 들어오는 위치
                .userService(memberService); // 해당 서비스로 유저 정보 받기 [loadUser 재정의]


    }


    @Autowired
    private MemberService memberService;

    @Override   // 인증(로그인) 관리 메소드
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(new BCryptPasswordEncoder());
            // 인증할 서비스 객체                   -> 패스워드 인코더(BCrypt 객체로)
    }
}
