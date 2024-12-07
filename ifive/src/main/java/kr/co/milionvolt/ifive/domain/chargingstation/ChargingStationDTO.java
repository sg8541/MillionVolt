package kr.co.milionvolt.ifive.domain.chargingstation;

import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import lombok.*;

import java.math.BigDecimal;

// 카카오 지도 API는 주소만으로 위치를 표시할 수 있기 때문에
// 위도와 경도는 넣지 않았음.

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChargingStationDTO {

  private Integer stationId;
  private String name;
  private String address;
  private Integer totalCharger;
  private Integer availableCharger; // 사용가능한 충전기 개수
  private Integer charger_7kW_count;
  private Integer charger_50kW_count;
  private Integer charger_100kW_count;
  private Integer charger_200kW_count;
  private Integer charger_300kW_plus_count;
  private ChargeSpeed chargeSpeed;
  private BigDecimal pricePerKWh;
  private java.sql.Timestamp createdAt;
  private String filePath;
  private ChagerType chagerType;
  private ChargerStatus chargerStatus;

  public enum ChargeSpeed {
    slow, medium, fast, super_fast
  }

  public enum ChargerStatus {
    available, in_use, maintenance
  }

  public enum ChagerType {
    SEVEN_KW,  // 7kW
    FIFTY_KW,  // 50kW
    ONE_HUNDRED_KW, // 100kW
    TWO_HUNDRED_KW,  // 200kW
    THREE_HUNDRED_PLUS_KW // 300kW+
  }

}
