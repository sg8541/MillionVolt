package kr.co.milionvolt.ifive.controller.reservation;

import kr.co.milionvolt.ifive.domain.payment.CancelPaymentDTO;
import kr.co.milionvolt.ifive.domain.reservation.CancelReservationDTO;
import kr.co.milionvolt.ifive.service.reservation.CancelReservationServiceImpl;
import kr.co.milionvolt.ifive.service.reservation.ReservationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reservation")
public class CancelReservationController {

    @Autowired
    CancelReservationServiceImpl cancelReservationService;

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelReservation(@RequestBody CancelReservationDTO cancelReservationDTO) {
        try{
            String result = cancelReservationService.cancelReservation(cancelReservationDTO.getUserId(), cancelReservationDTO.getReservationId());
            return ResponseEntity.ok("예약 취소가 완료되었습니다.");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("결제 취소 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
