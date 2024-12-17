package kr.co.milionvolt.ifive.domain.userinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCarInfoDTO {
    private String carNumber;
    private String chargerSpeedId;
    private String modelId;
    private BigDecimal carBattery;
}
