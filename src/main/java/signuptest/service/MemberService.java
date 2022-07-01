package signuptest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import signuptest.domain.MemberEntity;
import signuptest.domain.MemberRepository;
import signuptest.dto.LoginDto;
import signuptest.dto.MemberDto;
import signuptest.dto.Oauth2Dto;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class MemberService implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired // 자동 빈(메모리) 생성
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

    // 일반 회원 로그인
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
                            .authorities(Collections.unmodifiableSet( new LinkedHashSet<>(authorities))) // 부여된 인증 리스트
                            .build();
            return loginDto;    // logindto 세션 부여
        }else{ // 찾은 entity가 null이면
            return null;    // 로그인 실패시 null
        }

    }

    // Oauth 회원 로그인
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
                             // OAuth2UserRequest : 인증 결과를 호출 클래스
        // 인증[로그인] 결과 정보 요청
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 클라이언트 아이디 : oauth 구분용으로 사용할 변수
        String registrationid = userRequest.getClientRegistration().getRegistrationId();
        // 인증 정보(로그인) 결과 내용
        Map<String, Object> attributes = oAuth2User.getAttributes();
        // 회원정보(json) 호출시 사용되는 키
        String nameAttributeKey = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        // 네이버 로그인인지 카카오 로그인인지 판단 후 Oauth2Dto로 꺼내오기
        Oauth2Dto oauth2Dto = Oauth2Dto.sns확인(registrationid,attributes,nameAttributeKey);

        // 로그인된 아이디가 DB에 존재하는지 판단
        Optional<MemberEntity> optional = memberRepository.findBymid(oauth2Dto.getMid());
        if (!optional.isPresent()) {    // 아이디가 존재하지 않으면
            memberRepository.save(oauth2Dto.toentity());    // oauth2Dto를 엔티티로 변환 후 DB에 저장
        }else{  // 아이디가 존재하면
            // 로그인 한 적이 있으면 db 업데이트 처리
        }

        System.out.println("클라이언트 등록 이름 : "+registrationid);
        System.out.println("회원정보(json) 호출시 사용되는 키 : "+nameAttributeKey);
        System.out.println("인증 정보(로그인) 결과 내용 : "+oAuth2User.getAttributes());

        // 반환타입 DefaultOAuth2User ( 권한(role)명 , 회원인증정보 , 회원정보 호출키 )
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("SNS사용자")),    // 부여된 인증 키
                attributes, // 인증 정보(로그인) 결과 내용
                nameAttributeKey ); // 회원정보(json) 호출시 사용되는 키
    }

    public String 현재인증된회원아이디호출(){
        // 인증에 대한 결과값이 담겨있는 Authentication 객체 꺼내기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // principal = 유저 정보가 담겨있는 객체
        Object principal = authentication.getPrincipal();
        String mid = null;  // mid를 return 해주기 위해 메모리할당
        if(principal!=null){    // 인증(로그인) 된 상태
            if(principal instanceof UserDetails){   // 일반회원으로 로그인 한 경우
                mid = ((UserDetails) principal).getUsername();
            }else if(principal instanceof OAuth2User){  // oauth로 로그인 한 경우
                Map<String, Object>  attributes =((OAuth2User) principal).getAttributes();
                if(attributes.get("response") != null){ // 네이버일 경우
                    Map<String,Object> map = (Map<String, Object>) attributes.get("response");
                    mid = map.get("email").toString();
                }else if(attributes.get("kakao_account")!=null){  // 카카오일 경우
                    Map<String,Object> map = (Map<String, Object>) attributes.get("kakao_account");
                    mid = map.get("email").toString();
                }else{

                }
            }
            return mid;
        }else{  // 인증(로그인) 안되어 있는 상태
            return mid;
        }
    }



}
