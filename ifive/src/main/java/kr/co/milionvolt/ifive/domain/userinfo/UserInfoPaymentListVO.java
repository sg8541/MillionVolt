package kr.co.milionvolt.ifive.domain.userinfo;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoPaymentListVO {
  private Integer paymentId;
  private String name;
  private String amount;
  private BigDecimal chargedEnergy;
  private Integer chargerId;
  private PaymentMethod paymentMethod;
  private PaymentStatus paymentStatus;
  private String updatedAt;
  private String createdAt;
  private String chargeStart;
  private String chargeEnd;

  public enum PaymentMethod {
    PayPal, 카카오페이, 신용카드
  }

  public enum PaymentStatus {
    pending, completed, failed
  }
}
