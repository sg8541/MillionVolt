package kr.co.milionvolt.ifive.domain;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserCarDTO {

  private Integer carId;
  private String carNumber;
  private Integer chargerType;
  private Integer modelId;
  private BigDecimal carBattery;

}
