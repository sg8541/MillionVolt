package kr.co.milionvolt.ifive.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PasswordDTO {
    private String password;
    private String newPassword;
}
