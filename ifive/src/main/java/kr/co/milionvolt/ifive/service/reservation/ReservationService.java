package kr.co.milionvolt.ifive.service.reservation;

import kr.co.milionvolt.ifive.domain.reservation.ReservationDTO;

import java.sql.Timestamp;

public interface ReservationService {
    boolean saveReservation(ReservationDTO reservationDTO);
}
