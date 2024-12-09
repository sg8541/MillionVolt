package kr.co.milionvolt.ifive.domain.reservation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class ReservationListDTO {
    private int reservationId;
    private String status;
    private java.sql.Timestamp startTime;
    private java.sql.Timestamp endTime;
}
