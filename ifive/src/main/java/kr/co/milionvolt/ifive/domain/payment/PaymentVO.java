package kr.co.milionvolt.ifive.domain.payment;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PaymentVO {
    private Integer paymentId;
    private Integer userId;
    private Integer reservationId;
    private BigDecimal amount;
//    private PaymentDTO.PaymentMethod paymentMethod;
//    private PaymentDTO.PaymentStatus paymentStatus;
    private String paymentMethod;
    private String paymentStatus;

    public PaymentVO(Integer paymentId, Integer userId, Integer reservationId, BigDecimal amount, String paymentMethod, String paymentStatus) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.reservationId = reservationId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }
}
