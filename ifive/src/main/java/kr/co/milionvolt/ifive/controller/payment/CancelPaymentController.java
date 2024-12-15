package kr.co.milionvolt.ifive.controller.payment;

import kr.co.milionvolt.ifive.domain.payment.CancelPaymentDTO;
import kr.co.milionvolt.ifive.service.payment.CancelPaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
public class CancelPaymentController {

    @Autowired
    private CancelPaymentServiceImpl cancelPaymentServiceImpl;

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelPayment(@RequestBody CancelPaymentDTO cancelPaymentDTO) {
        try {
            String result = cancelPaymentServiceImpl.cancelPayment(cancelPaymentDTO.getUserId(), cancelPaymentDTO.getPaymentId());
            return ResponseEntity.ok(result); // 성공적으로 처리된 경우
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("결제 취소 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
