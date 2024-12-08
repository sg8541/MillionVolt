package kr.co.milionvolt.ifive.domain.payment;

import lombok.*;

import java.math.BigDecimal;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoPaymentListVO {
  private Integer paymentId;
  private BigDecimal amount;
  private PaymentMethod paymentMethod;
  private PaymentStatus paymentStatus;
  private java.sql.Timestamp createdAt;
  private java.sql.Timestamp chargeStart;
  private java.sql.Timestamp chargeEnd;
  private String address;

  public enum PaymentMethod {
    PayPal, 카카오페이, 신용카드
  }

  public enum PaymentStatus {
    pending, completed, failed
  }
}
