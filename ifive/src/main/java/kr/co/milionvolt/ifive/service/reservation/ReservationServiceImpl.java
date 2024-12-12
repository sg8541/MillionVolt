package kr.co.milionvolt.ifive.service.reservation;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import kr.co.milionvolt.ifive.domain.reservation.ReservationDTO;
import kr.co.milionvolt.ifive.mapper.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    public IamportClient iamportClient;

    @Override
    public boolean saveReservation(ReservationDTO reservationDTO) throws IamportResponseException, IOException {

        IamportResponse<Payment> response = iamportClient.paymentByImpUid(reservationDTO.getImpUid());
        Payment payment = response.getResponse();

        try {
            if(reservationMapper.checkConflictReservation(reservationDTO) == 0 && payment != null) {
                reservationDTO.setCreatedAt(LocalDateTime.now());
                return reservationMapper.insertReservation(reservationDTO)>0;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println("reservation error: " + e.getMessage());
            return false;
        }
    }
}
