package kr.co.milionvolt.ifive.domain.user;

import lombok.*;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupDTO {
  private String username;
  private String userId;
  private String email;
  private String phoneNumber;
  private String password;
  private String carNumber;
  private Integer chargerTypeId;
  private Integer modelId;
  private BigDecimal carBattery;
}

