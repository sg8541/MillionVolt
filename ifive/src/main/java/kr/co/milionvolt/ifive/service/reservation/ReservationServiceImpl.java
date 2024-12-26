//package kr.co.milionvolt.ifive.service.reservation;
//
//import com.siot.IamportRestClient.IamportClient;
//import com.siot.IamportRestClient.exception.IamportResponseException;
//import com.siot.IamportRestClient.response.IamportResponse;
//import com.siot.IamportRestClient.response.Payment;
//import kr.co.milionvolt.ifive.domain.reservation.ReservationDTO;
//import kr.co.milionvolt.ifive.entity.ReservationRedis;
//import kr.co.milionvolt.ifive.mapper.ReservationMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//
//@Service
//public class ReservationServiceImpl implements ReservationService {
//
//    @Autowired
//    private ReservationMapper reservationMapper;
//
//    @Autowired
//    private ReservationRedisService reservationRedisService;
//
//    @Autowired
//    public IamportClient iamportClient;
//
//    @Override
//    @Transactional
//    public String saveReservation(ReservationDTO reservationDTO) throws IamportResponseException, IOException {
//
//        IamportResponse<Payment> response = iamportClient.paymentByImpUid(reservationDTO.getImpUid());
//        Payment payment = response.getResponse();
//
//        String message = "";
//        ZonedDateTime koreaStartTime = reservationDTO.getStartTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Seoul"));
//        ZonedDateTime koreaEndTime = reservationDTO.getEndTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Seoul"));
//
//        reservationDTO.setStartTime(koreaStartTime.toLocalDateTime());
//        reservationDTO.setEndTime(koreaEndTime.toLocalDateTime());
//        try {
//            if(reservationMapper.checkConflictReservation(reservationDTO) != 0) {
//                message = "이미 예약된 시간이 있습니다. 시간을 확인해주세요.";
//            } else if(reservationMapper.checkConflictReservation(reservationDTO) == 0 && payment != null) {
//                reservationDTO.setCreatedAt(LocalDateTime.now());
//
//                message =  "예약이 완료되었습니다.";
//                    int num =  reservationMapper.insertReservation(reservationDTO);
//                        if(num != 0){
//                            ReservationRedis reservationRedis = new ReservationRedis();
//                            reservationRedis.setReservationId(reservationDTO.getReservationId());
//                            reservationRedis.setStartTime(reservationDTO.getStartTime());
//                            reservationRedis.setEndTime(reservationDTO.getEndTime());
//                            reservationRedis.setUserId(reservationDTO.getUserId());
//                            reservationRedis.setStationId(reservationDTO.getStationId());
//                            reservationRedisService.save(reservationRedis);
//                        }
//                return message;
//            }
//        } catch (Exception e) {
//            System.err.println("reservation error: " + e.getMessage());
//            return e.getMessage();
//        }
//        return message;
//    }
//}

package kr.co.milionvolt.ifive.service.reservation;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
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
        String message = "";

        try {
            // 1. 예약 시간 변환
            ZonedDateTime koreaStartTime = reservationDTO.getStartTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Seoul"));
            ZonedDateTime koreaEndTime = reservationDTO.getEndTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Seoul"));
            reservationDTO.setStartTime(koreaStartTime.toLocalDateTime());
            reservationDTO.setEndTime(koreaEndTime.toLocalDateTime());

            // 2. 예약 중복 확인
            if (reservationMapper.checkConflictReservation(reservationDTO) != 0) {
                return "이미 예약된 시간이 있습니다. 시간을 확인해주세요.";
            }

            // 3. 결제 정보 확인
            IamportResponse<Payment> response = iamportClient.paymentByImpUid(reservationDTO.getImpUid());
            Payment payment = response.getResponse();

            if (payment == null || !"paid".equals(payment.getStatus())) {
                return "결제 확인 중 오류가 발생했습니다.";
            }

            // 4. DB에 예약 저장
            reservationDTO.setCreatedAt(LocalDateTime.now());
            reservationMapper.insertReservation(reservationDTO);

            // 5. Redis에 예약 정보 저장
            ReservationRedis reservationRedis = new ReservationRedis();
            reservationRedis.setReservationId(reservationDTO.getReservationId());
            reservationRedis.setStartTime(reservationDTO.getStartTime());
            reservationRedis.setEndTime(reservationDTO.getEndTime());
            reservationRedis.setUserId(reservationDTO.getUserId());
            reservationRedis.setStationId(reservationDTO.getStationId());
            reservationRedisService.save(reservationRedis);

            message = "예약이 완료되었습니다.";
        } catch (Exception e) {
            try {
                CancelData cancelData = new CancelData(reservationDTO.getImpUid(), true);
                iamportClient.cancelPaymentByImpUid(cancelData);
            } catch (IamportResponseException | IOException cancelEx) {
                System.err.println("결제 취소 실패: " + cancelEx.getMessage());
            }
            System.err.println("예약 저장 실패: " + e.getMessage());
            return "예약 처리 중 문제가 발생했습니다. 다시 시도해주세요.";
        }
        return message;
    }
}





