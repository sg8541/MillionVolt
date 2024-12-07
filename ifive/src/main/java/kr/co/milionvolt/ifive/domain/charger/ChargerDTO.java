package kr.co.milionvolt.ifive.domain.charger;

import lombok.*;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChargerDTO {

  private Integer chargerId;
  private Integer stationId;
  private ChagerType chagerType;
  private ChargerStatus chargerStatus;
  private java.sql.Timestamp createdAt;

  // enum은 영문자와 언더스코어만 사용가능하다고 이렇게 해야한다고 함
  // 뷰에서 전환할것임
  public enum ChagerType {
    SEVEN_KW,  // 7kW
    FIFTY_KW,  // 50kW
    ONE_HUNDRED_KW, // 100kW
    TWO_HUNDRED_KW,  // 200kW
    THREE_HUNDRED_PLUS_KW; // 300kW+
  }

  public enum ChargerStatus {
    available, in_use, maintenance
  }
}
