package kr.co.milionvolt.ifive.domain.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsVO {
    private Integer id;
    private String userId;
    private String password;
    private String userName;
    private String role;
    private BigDecimal carBattery;
    private Integer modelBattery;

}
