package kr.co.milionvolt.ifive.domain;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PenaltieDTO {

  private Integer penaltyId;
  private BigDecimal penaltyAmount;
  private String reason;
  private java.sql.Timestamp createdAt;

}
