package kr.co.milionvolt.ifive.service.reservation;

import kr.co.milionvolt.ifive.domain.reservation.ReservationListDTO;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationListService {
    List<ReservationListDTO> printReservationList(LocalDateTime startTime, LocalDateTime endTime);
}
