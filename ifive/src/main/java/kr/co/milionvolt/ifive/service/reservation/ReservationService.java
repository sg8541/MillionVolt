package kr.co.milionvolt.ifive.service.reservation;

import kr.co.milionvolt.ifive.domain.reservation.ReservationDTO;

public interface ReservationService {
    boolean saveReservation(ReservationDTO reservationDTO);
}
