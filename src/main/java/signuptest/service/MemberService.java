package signuptest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import signuptest.domain.MemberEntity;
import signuptest.domain.MemberRepository;
import signuptest.dto.LoginDto;
import signuptest.dto.MemberDto;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class MemberService implements UserDetailsService {

    @Autowired
    MemberRepository memberRepository;

    // 회원가입
    @Transactional
    public void signup(MemberDto memberDto){
        Optional<MemberEntity> optionalMember = memberRepository.findBymid(memberDto.getMid()); // 아이디를 이용해 엔티티 찾기
        if(optionalMember.isPresent()){ // 찾은 entity가 null이 아니면
            // 회원가입 처리 X
        }else{ // 찾은 entity가 null이면
            MemberEntity memberEntity = memberDto.toentity();   // 인수로 받은 DTO -> Entity 변환
            memberRepository.save(memberEntity);    // 회원가입 처리
        }
    }

    // 로그인
    @Override
    public UserDetails loadUserByUsername(String mid) throws UsernameNotFoundException {
        // 아이디를 이용해 엔티티 찾기
        Optional<MemberEntity> optionalMember = memberRepository.findBymid(mid);
        if(optionalMember.isPresent()){  // 찾은 entity가 null이 아니면
            MemberEntity memberEntity = optionalMember.get();   // Optinal을 MemberEntity로 변환
            List<GrantedAuthority> authorities = new ArrayList<>(); // 해당 entity의 권한을 리스트에 담기
                // GrantedAuthority : 부여된 인증의 클래스
                // List<GrantedAuthority> : 부여된 인증들을 모아두기
            authorities.add(new SimpleGrantedAuthority(memberEntity.getRole().getKey()));   // 리스트에 인증된 엔티티의 권한 추가
            // builder를 이용해 entity의 정보를 logindto에 저장
            LoginDto loginDto = LoginDto.builder()
                            .mid(memberEntity.getMid()) // 회원 아이디
                            .mpassword(memberEntity.getMpassword()) // 회원 비밀번호
                            .authorities(Collections.unmodifiableSet(new LinkedHashSet<>(authorities))) // 부여된 인증 리스트
                            .build();
            return loginDto;    // logindto 세션 부여
        }else{ // 찾은 entity가 null이면
            return null;    // 로그인 실패시 null
        }

    }
}
