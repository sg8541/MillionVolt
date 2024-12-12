package kr.co.milionvolt.ifive.domain.payment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Setter
@Getter
@ToString
public class PayPriceDTO {
    private int paymentId;
    private int userId;
    private int reservationId;
    private long amount;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime chargeStart;
    private LocalDateTime chargeEnd;
    private String impUid;
}
