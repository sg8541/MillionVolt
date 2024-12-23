package kr.co.milionvolt.ifive.controller.reservation;

import kr.co.milionvolt.ifive.domain.reservation.ReservationDTO;
import kr.co.milionvolt.ifive.service.reservation.ReservationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ReservationController {

    @Autowired
    private ReservationServiceImpl reservationServiceImpl;

    @PostMapping("/api/v1/reservation/{imp_uid}")
    @Transactional
    public ResponseEntity<Map<String, Object>> reservation(@PathVariable String imp_uid, @RequestBody ReservationDTO reservationDTO) {
        Map<String, Object> response = new HashMap<>();
        System.out.println(imp_uid);
        System.out.println(reservationDTO);

        try {;
            reservationDTO.setImpUid(imp_uid);
            String success = reservationServiceImpl.saveReservation(reservationDTO);

            if (success.equals("예약이 완료되었습니다.")) {
                response.put("message", "예약이 완료되었습니다.");
                return ResponseEntity.ok(response);
            } else if(success.equals("이미 예약된 시간이 있습니다. 시간을 확인해주세요.")){
                response.put("message", "이미 예약된 시간이 있습니다. 시간을 확인해주세요.");
                return ResponseEntity.status(409).body(response);
            } else if(success.equals("이미 예약된 시간의 15분 전후는 예약을 할 수 없습니다.")){
                return ResponseEntity.status(409).body(response);
            }
        } catch (Exception e) {
            response.put("message", "예약 처리 중 오류가 발생했습니다.");
            System.err.println("controller" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
        return ResponseEntity.status(200).body(response);
    }
}
