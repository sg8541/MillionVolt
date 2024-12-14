package kr.co.milionvolt.ifive.domain.userinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarBatteryAndChargerSpeedUpdateDTO {
    private Integer carId;
    private Integer chargerSpeedId;
    private BigDecimal carBattery;
}
