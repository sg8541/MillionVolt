package kr.co.milionvolt.ifive.service.reservation;

import com.siot.IamportRestClient.exception.IamportResponseException;

import java.io.IOException;

public interface CancelReservationService {
    public String cancelReservation(int userId, int reservationId) throws IamportResponseException, IOException;
}
