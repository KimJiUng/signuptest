package signuptest.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    // 열거형: MEMBER [0] , ADMIN [1]
    MEMBER("ROLE_MEMBER","회원"),
    ADMIN("ROLE_ADMIN","관리자");

    // final = 상수[데이터 고정]
    private final String key;
    private final String keyword;
}
