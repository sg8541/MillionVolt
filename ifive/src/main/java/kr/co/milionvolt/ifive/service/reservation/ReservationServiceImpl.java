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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private ReservationRedisService reservationRedisService;

    @Autowired
    public IamportClient iamportClient;

    @Override
    @Transactional
    public String saveReservation(ReservationDTO reservationDTO) throws IamportResponseException, IOException {

        IamportResponse<Payment> response = iamportClient.paymentByImpUid(reservationDTO.getImpUid());
        Payment payment = response.getResponse();

        String message = "";
        ZonedDateTime koreaStartTime = reservationDTO.getStartTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        ZonedDateTime koreaEndTime = reservationDTO.getEndTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Seoul"));

        reservationDTO.setStartTime(koreaStartTime.toLocalDateTime());
        reservationDTO.setEndTime(koreaEndTime.toLocalDateTime());
        try {
            if(reservationMapper.checkConflictReservation(reservationDTO) != 0) {
                message = "이미 예약된 시간이 있습니다. 시간을 확인해주세요.";
            } else if(reservationMapper.checkConflictReservation(reservationDTO) == 0 && payment != null) {
                reservationDTO.setCreatedAt(LocalDateTime.now());

                message =  "예약이 완료되었습니다.";
                    int num =  reservationMapper.insertReservation(reservationDTO);
                        if(num != 0){
                            ReservationRedis reservationRedis = new ReservationRedis();
                            reservationRedis.setReservationId(reservationDTO.getReservationId());
                            reservationRedis.setStartTime(reservationDTO.getStartTime());
                            reservationRedis.setEndTime(reservationDTO.getEndTime());
                            reservationRedis.setUserId(reservationDTO.getUserId());
                            reservationRedis.setStationId(reservationDTO.getStationId());
                            reservationRedisService.save(reservationRedis);
                        }
                return message;
            }
        } catch (Exception e) {
            System.err.println("reservation error: " + e.getMessage());
            return e.getMessage();
        }
        return message;
    }
}
