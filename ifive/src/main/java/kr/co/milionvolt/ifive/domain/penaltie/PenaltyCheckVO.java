package kr.co.milionvolt.ifive.domain.penaltie;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PenaltyCheckVO {
    private int penaltyId;
    private int penaltyAmount;
}
