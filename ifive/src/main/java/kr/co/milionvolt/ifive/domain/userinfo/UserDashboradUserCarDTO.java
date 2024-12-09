package kr.co.milionvolt.ifive.domain.userinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDashboradUserCarDTO {
    private String username;
    private BigDecimal carBattery;
    private String modelName;
    private Integer modelBattery;
    private String modelFilepath;
    private String chargerType;
}
