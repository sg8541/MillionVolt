package kr.co.milionvolt.ifive.service.reservation;

import com.siot.IamportRestClient.exception.IamportResponseException;
import kr.co.milionvolt.ifive.domain.reservation.ReservationDTO;

import java.io.IOException;
import java.sql.Timestamp;

public interface ReservationService {
    boolean saveReservation(ReservationDTO reservationDTO) throws IamportResponseException, IOException;
}
