package kr.co.milionvolt.ifive.domain.userinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserInfoDTO {
    private Integer id;
    private String username;
    private String email;
    private String phoneNumber;
}
