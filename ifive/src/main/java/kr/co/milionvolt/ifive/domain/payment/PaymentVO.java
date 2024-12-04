package kr.co.milionvolt.ifive.domain.payment;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PaymentVO {
    private Integer paymentId;
    private Integer userId;
    private Integer reservationId;
    private BigDecimal amount;
    private PaymentDTO.PaymentMethod paymentMethod;
    private PaymentDTO.PaymentStatus paymentStatus;

    public PaymentVO(Integer paymentId, Integer userId, Integer reservationId, BigDecimal amount, PaymentDTO.PaymentMethod paymentMethod, PaymentDTO.PaymentStatus status) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.reservationId = reservationId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = status;
    }
}
