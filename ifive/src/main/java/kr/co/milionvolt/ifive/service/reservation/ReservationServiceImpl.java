package kr.co.milionvolt.ifive.service.reservation;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import kr.co.milionvolt.ifive.domain.reservation.ReservationDTO;
import kr.co.milionvolt.ifive.entity.ReservationRedis;
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
    private ReservationRedisService reservationRedisService;

    @Autowired
    public IamportClient iamportClient;

    @Override
    public boolean saveReservation(ReservationDTO reservationDTO) throws IamportResponseException, IOException {

        IamportResponse<Payment> response = iamportClient.paymentByImpUid(reservationDTO.getImpUid());
        Payment payment = response.getResponse();

        try {
            if(reservationMapper.checkConflictReservation(reservationDTO) == 0 && payment != null) {
                reservationDTO.setCreatedAt(LocalDateTime.now());
                    int num =  reservationMapper.insertReservation(reservationDTO);
                        if(num != 0){
                            ReservationRedis reservationRedis = new ReservationRedis();
                            reservationRedis.setReservationId(reservationDTO.getReservationId());
                            reservationRedis.setStartTime(reservationDTO.getStartTime());
                            reservationRedis.setEndTime(reservationDTO.getEndTime());
                            reservationRedis.setUserId(reservationDTO.getUserId());
                            reservationRedisService.save(reservationRedis);
                        }
                return num > 0; //num의 값이 없을 경우 ==  0
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println("reservation error: " + e.getMessage());
            return false;
        }
    }
}
