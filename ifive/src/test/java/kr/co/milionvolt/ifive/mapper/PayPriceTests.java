package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.payment.PayPriceDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@SpringBootTest
public class PayPriceTests {

    @Autowired
    PayPriceMapper payPriceMapper;

    PayPriceDTO payPriceDTO = new PayPriceDTO();

    @Test
    public void test() {
        // DTO에 값 설정
//        payPriceDTO.setPaymentId(2);  // payment_id 설정
        payPriceDTO.setUserId(1);      // user_id 설정
        payPriceDTO.setReservationId(72); // reservation_id 설정
        payPriceDTO.setStationId(1);
        payPriceDTO.setAmount(20000);   // amount 설정
        payPriceDTO.setPaymentMethod("카카오페이"); // payment_method 설정
        payPriceDTO.setPaymentStatus("pending");    // payment_status 설정

        // 날짜 설정
        payPriceDTO.setCreatedAt(LocalDateTime.now()); // created_at 설정
        payPriceDTO.setUpdatedAt(LocalDateTime.now()); // updated_at 설정
        payPriceDTO.setChargeStart(Timestamp.valueOf(LocalDateTime.now())); // charge_start 설정
        payPriceDTO.setChargeEnd(Timestamp.valueOf(LocalDateTime.now().plusHours(1))); // charge_end 설정
        payPriceDTO.setImpUid("imp50578251");
        // DB에 데이터 삽입
        boolean result = payPriceMapper.insertPayPrice(payPriceDTO);

        // 결과 출력 (삽입된 행의 수 확인)
        System.out.println(result);  // 1이면 정상적으로 삽입됨
    }
}
