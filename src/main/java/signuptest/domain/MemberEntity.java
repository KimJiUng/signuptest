package signuptest.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter@Setter@ToString
@NoArgsConstructor@AllArgsConstructor
@Builder
public class MemberEntity {

    @Id // pk 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 설정
    private int mno;    // 회원번호
    private String mid; // 회원아이디
    private String mpassword;   // 회원비밀번호
    private String mname;   // 회원이름
    private String memail;  // 회원 메일
    @Enumerated(EnumType.STRING) // 열거형 이름
    private Role role;  // 권한

    public String getrolekey() {
        return role.getKey();
    } // 시큐리티에서 인증허가 된 리스트에 보관하기 위해

}
