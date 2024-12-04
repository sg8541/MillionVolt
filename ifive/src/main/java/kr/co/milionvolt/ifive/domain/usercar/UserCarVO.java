package kr.co.milionvolt.ifive.domain.usercar;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class UserCarVO {
    private Integer carId;
    private String carNumber;
    private Integer chargerType;
    private Integer modelId;
    private BigDecimal carBattery;

    public UserCarVO(Integer carId, String carNumber, Integer chargerType, Integer modelId, BigDecimal carBattery) {
        this.carId = carId;
        this.carNumber = carNumber;
        this.chargerType = chargerType;
        this.modelId = modelId;
        this.carBattery = carBattery;
    }
}
