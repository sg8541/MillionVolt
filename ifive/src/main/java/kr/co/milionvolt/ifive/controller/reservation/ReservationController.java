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

    @PostMapping("/api/reservation")
    public ResponseEntity<Map<String, Object>> reservation(@RequestBody ReservationDTO reservationDTO) {
        boolean success = reservationServiceImpl.saveReservation(reservationDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("message", success ? "success" : "failure");
        return ResponseEntity.ok(response);
    }
}
