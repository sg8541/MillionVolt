package kr.co.milionvolt.ifive.domain.payment;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

  private Integer paymentId;
  private Integer userId;
  private Integer reservationId;
  private BigDecimal amount;
  private String paymentMethod;
  private String paymentStatus;
  private java.sql.Timestamp createdAt;
  private java.sql.Timestamp updatedAt;
  private java.sql.Timestamp chargeStart;
  private java.sql.Timestamp chargeEnd;

//  public enum PaymentMethod {
//    PayPal, 카카오페이, 신용카드
//  }
//
//  public enum PaymentStatus {
//    pending, completed, failed
//  }
}
