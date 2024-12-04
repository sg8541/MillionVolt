package kr.co.milionvolt.ifive.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserVO {
    private int id;
    private String username;
    private String user_id;
    private String email;
    private String phone_number;
    private String password;
    private String level_id;
    private Timestamp created_at;
}
