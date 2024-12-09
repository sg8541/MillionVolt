package kr.co.milionvolt.ifive.service.reservation;

import kr.co.milionvolt.ifive.domain.reservation.ReservationListDTO;

import java.sql.Timestamp;
import java.util.List;

public interface ReservationListService {
    List<ReservationListDTO> printReservationList(Timestamp startTime, Timestamp endTime);
}
