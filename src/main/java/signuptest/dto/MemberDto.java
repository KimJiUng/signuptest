package signuptest.dto;

import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import signuptest.domain.MemberEntity;
import signuptest.domain.Role;

@Getter@Setter@ToString
@NoArgsConstructor@AllArgsConstructor
@Builder
public class MemberDto {

    private int mno;    // 회원 번호
    private String mid; // 회원 아이디
    private String mpassword;   // 회원 비밀번호
    private String mname;   // 회원 이름
    private String memail;  // 회원 이메일

    // dto -> entity
    public MemberEntity toentity(){
        // BCrypt 를 통한 패스워드 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // builder를 통해 entity 생성
        return MemberEntity.builder()
                .mid(this.mid)  // 회원 아이디
                .mpassword(passwordEncoder.encode(this.mpassword))  // BCrypt로 인코딩한 회원 비밀번호
                .mname(this.mname)  // 회원 이름
                .memail(this.memail) // 회원 이메일
                .role(Role.MEMBER)  // 권한 부여
                .build();
    }

}
