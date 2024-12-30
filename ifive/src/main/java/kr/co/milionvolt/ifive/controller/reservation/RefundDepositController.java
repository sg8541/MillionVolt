package kr.co.milionvolt.ifive.controller.reservation;

import com.siot.IamportRestClient.exception.IamportResponseException;
import kr.co.milionvolt.ifive.service.reservation.RefundDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class RefundDepositController {

    @Autowired
    RefundDepositService refundDepositService;

    @PostMapping("/refundDeposit")
    public ResponseEntity <Map<String, Object>> refund(@RequestBody Map<String, Integer> request) {
        Map<String, Object> response = new HashMap<>();
        int reservationId = request.get("reservationId");
        System.out.println(reservationId);
        try {
            int refundResult = refundDepositService.progressRefundDeposit(reservationId);

            if (refundResult == 0) {
                response.put("success", true);
                response.put("message", "환불 신청이 완료되었습니다.");
            }else{
                response.put("success", false);
                response.put("message", "환불 받을 금액이 없습니다.");
            }

            return ResponseEntity.ok(response);
        } catch (IamportResponseException | IOException e) {
            response.put("success", false);
            response.put("message", "환불 처리 중 문제가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
