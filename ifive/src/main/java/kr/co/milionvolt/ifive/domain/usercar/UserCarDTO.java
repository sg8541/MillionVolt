package kr.co.milionvolt.ifive.domain.usercar;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCarDTO {
  private Integer carId;
  private String carNumber;
  private Integer chargerType;
  private Integer modelId;
  private BigDecimal carBattery;

}
