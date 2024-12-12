package kr.co.milionvolt.ifive.domain.chargingstation;
import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

// 카카오 지도 API는 주소만으로 위치를 표시할 수 있기 때문에
// 위도와 경도는 넣지 않았음.

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChargingStationDTO {

  // 자동 pk
  private Integer stationId;

  // 충전소명
  private String name;

  // 충전소 위치
  private String address;

  // 충전기 총 갯수
  private Integer totalCharger;

  // 사용가능한 충전기 개수
  private Integer availableCharger;

  // 충전기 급속, 완속
  private Integer chargerSpeedId;

  // 충전기 요금
  private BigDecimal pricePerKWh;

  // 충전소 사진
  private String filePath;

  // 시설구분
  private String facilityType;

  // 기종 정보(급속,완속)
  private String deviceType;

  //충전기 타입(AC, DC등)
  private String chargerType;

  // 임의로 조정(충전기 상태)
  private Integer chargerStatusId;

  private List<ChargerDTO> chargers;

}