package kr.co.milionvolt.ifive.domain.chargingstation;

import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import lombok.Getter;

import java.math.BigDecimal;


// ChargerDTO 추가
@Getter
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
    private ChargingStationDTO.ChargeSpeed chargeSpeed;
    private BigDecimal pricePerKWh;
    private String filePath;
    private ChargerDTO.ChagerType chagerType;
    private ChargerDTO.ChargerStatus chargerStatus;

    public ChargingStationVO(Integer stationId, String name, String address,
                             Integer totalCharger, Integer availableCharger,
                             Integer charger_7kW_count, Integer charger_50kW_count, Integer charger_100kW_count, Integer charger_200kW_count, Integer charger_300kW_plus_count,
                             ChargingStationDTO.ChargeSpeed chargeSpeed,
                             BigDecimal pricePerKWh, String filePath,
                             ChargerDTO.ChagerType chagerType, ChargerDTO.ChargerStatus chargerStatus) {
        this.stationId = stationId;
        this.name = name;
        this.address = address;
        this.totalCharger = totalCharger;
        this.availableCharger = availableCharger;
        this.charger_7kW_count = charger_7kW_count;
        this.charger_50kW_count = charger_50kW_count;
        this.charger_100kW_count = charger_100kW_count;
        this.charger_200kW_count = charger_200kW_count;
        this.charger_300kW_plus_count = charger_300kW_plus_count;
        this.chargeSpeed = chargeSpeed;
        this.pricePerKWh = pricePerKWh;
        this.filePath = filePath;
        this.chagerType = chagerType;
        this.chargerStatus = chargerStatus;
    }
}
