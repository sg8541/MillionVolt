package kr.co.milionvolt.ifive.controller.reservation;

import kr.co.milionvolt.ifive.domain.reservation.ReservationDTO;
import kr.co.milionvolt.ifive.service.reservation.ReservationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ReservationController {

    @Autowired
    ReservationServiceImpl reservationServiceImpl;

    @PostMapping("/api/v1/reservation")
    public ResponseEntity<Map<String, Object>> reservation(@RequestBody ReservationDTO reservationDTO) {

        boolean success = reservationServiceImpl.saveReservation(reservationDTO);

        Map<String, Object> response = new HashMap<>();

        if (success) {
            response.put("message", "예약이 완료되었습니다.");
        } else {
            response.put("message", "이미 예약된 시간이 있습니다. 시간을 확인해주세요.");
        }
        return ResponseEntity.ok(response);
    }
}
