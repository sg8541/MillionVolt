package kr.co.milionvolt.ifive.domain.reservation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Setter
@Getter
public class ReservationListDTO {
    private int reservationId;
    private int stationId;
    private int chargerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
