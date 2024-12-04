package kr.co.milionvolt.ifive.domain.penaltie;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PenaltieVO {
    private Integer penaltyId;
    private BigDecimal penaltyAmount;
    private String reason;

    public PenaltieVO(Integer penaltyId, BigDecimal penaltyAmount, String reason) {
        this.penaltyId = penaltyId;
        this.penaltyAmount = penaltyAmount;
        this.reason = reason;
    }
}
