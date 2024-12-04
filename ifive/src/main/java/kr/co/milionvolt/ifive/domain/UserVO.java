package kr.co.milionvolt.ifive.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
public class UserVO {
    private final Integer id;
    private final String username;
    private final String userId;
    private final String email;
    private final String phoneNumber;
    private final Timestamp createdAt;

    public UserVO(Integer id, String username, String userId, String email, String phoneNumber, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.userId = userId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
    }
}


