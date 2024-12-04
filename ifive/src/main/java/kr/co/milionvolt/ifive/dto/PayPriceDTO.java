package kr.co.milionvolt.ifive.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PayPriceDTO {
    private String reserverName;        // 예약자 이름
    private Double expectedAmount;     // 예상 금액
    private Double deposit;            // 보증금
    private String chargerType;        // 충전기 상태
    private String chargerNumber;      // 충전기 번호
    private String reservationCode;    // 예약 코드
}
