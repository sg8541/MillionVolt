package kr.co.milionvolt.ifive.domain.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FindFwdDTO {
    private String username;
    private String userId;
    private String email;
    private String password;
}
