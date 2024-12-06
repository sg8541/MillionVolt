package kr.co.milionvolt.ifive.domain.chargingstation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;



@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChargingStationVO {
    private Integer stationId;
    private String name;
    private String address;
    private Integer totalCharger;
    private Integer availableCharger;
    private Integer charger_7kW_count;
    private Integer charger_50kW_count;
    private Integer charger_100kW_count;
    private Integer charger_200kW_count;
    private Integer charger_300kW_plus_count;
    private ChargeSpeed chargeSpeed;
    private BigDecimal pricePerKWh;
    private String filePath;
    private ChagerType chagerType;
    private ChargerStatus chargerStatus;

    public enum ChargeSpeed {
        slow, medium, fast, super_fast
    }

    public enum ChagerType {
        SEVEN_KW,  // 7kW
        FIFTY_KW,  // 50kW
        ONE_HUNDRED_KW, // 100kW
        TWO_HUNDRED_KW,  // 200kW
        THREE_HUNDRED_PLUS_KW // 300kW+
    }

    public enum ChargerStatus {
        available, in_use, maintenance
    }
}
