package kr.co.milionvolt.ifive.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResetDTO {
    private String username;
    private String userId;
    private String email;
    private String password;
}
