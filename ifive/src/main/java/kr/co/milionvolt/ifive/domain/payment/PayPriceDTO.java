package kr.co.milionvolt.ifive.domain.payment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Setter
@Getter
@ToString
public class PayPriceDTO {
    private int userId;
    private int reservationId;
    private int stationId;
    private int chargerId;
    private long amount;
    private double chargedEnergy;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Timestamp chargeStart;
    private Timestamp chargeEnd;
    private String impUid;
}
