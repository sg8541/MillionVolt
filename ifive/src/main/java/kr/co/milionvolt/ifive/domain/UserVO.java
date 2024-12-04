package kr.co.milionvolt.ifive.domain;

import lombok.Getter;

@Getter
public class UserVO {
    private final Integer id;
    private final String username;
    private final String userId;
    private final String email;
    private final String phoneNumber;

    public UserVO(Integer id, String username, String userId, String email, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.userId = userId;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
