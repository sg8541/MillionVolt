package kr.co.milionvolt.ifive.domain.payment;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CancelPaymentDTO {
    int userId;
    int paymentId;
}
