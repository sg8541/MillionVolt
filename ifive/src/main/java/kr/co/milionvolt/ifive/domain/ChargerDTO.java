package kr.co.milionvolt.ifive.domain;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChargerDTO {

  private Integer chargerId;
  private ChagerType chagerType;
  private ChargerStatus chargerStatus;
  private java.sql.Timestamp createdAt;

  // enum은 영문자와 언더스코어만 사용가능하다고 이렇게 해야한다고 함...
  public enum ChagerType {
    SEVEN_KW,
    FIFTY_KW,
    ONE_HUNDRED_KW,
    TWO_HUNDRED_KW,
    THREE_HUNDRED_PLUS_KW;

    // 뷰에서 출력될 때 사용할 값을 반환하는 메서드
    public String getDisplayValue() {
      switch (this) {
        case SEVEN_KW:
          return "7kW";
        case FIFTY_KW:
          return "50kW";
        case ONE_HUNDRED_KW:
          return "100kW";
        case TWO_HUNDRED_KW:
          return "200kW";
        case THREE_HUNDRED_PLUS_KW:
          return "300kW 이상";
        default:
          return this.name();  // 기본값 (예상치 못한 값에 대한 처리)
      }
    }
  }

  public enum ChargerStatus {
    available, in_use, maintenance
  }


}
