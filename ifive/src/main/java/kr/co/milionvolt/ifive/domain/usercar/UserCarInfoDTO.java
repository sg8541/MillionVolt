package kr.co.milionvolt.ifive.domain.usercar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCarInfoDTO {
    private String carNumber;
    private String chargerType;
    private String modelName;
    private BigDecimal carBattery;
}
