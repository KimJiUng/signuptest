package signuptest.dto;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
public class LoginDto implements UserDetails {  // 로그인 세션에 넣을 dto

    private String mid; // 회원 아이디
    private String mpassword;   // 회원 비밀번호
    private Set<GrantedAuthority> authorities;  // 부여된 인증들



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {    // 부여된 인증들 반환
        return this.authorities;
    }

    @Override
    public String getPassword() {   // 패스워드 반환
        return this.mpassword;
    }

    @Override
    public String getUsername() {   // 아이디 반환
        return this.mid;
    }

    // 계정 인증 만료 여부 확인 [true : 만료되지 않음]
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠겨 있는지 확인 [true : 잠겨있지 않음]
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정 패스워드 만료 여부 확인 [true : 만료되지 않음]
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 사용 가능 여부 확인 [true : 사용가능]
    @Override
    public boolean isEnabled() { return true; }
}
