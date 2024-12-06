package kr.co.milionvolt.ifive.domain.payment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
public class PayPriceDTO {
    private int paymentId;            // Change from payment_id to paymentId
    private int userId;               // Change from user_id to userId
    private int reservationId;        // Change from reservation_id to reservationId
    private long amount;
    private String paymentMethod;     // Change from payment_method to paymentMethod
    private String paymentStatus;     // Change from payment_status to paymentStatus
    private LocalDateTime createdAt;  // Change from created_at to createdAt
    private LocalDateTime updatedAt;  // Change from updated_at to updatedAt
    private LocalDateTime chargeStart; // Change from charge_start to chargeStart
    private LocalDateTime chargeEnd;   // Change from charge_end to chargeEnd
    private String impUid;
}
