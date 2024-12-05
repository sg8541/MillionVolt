package kr.co.milionvolt.ifive.domain.charger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChargerVO {
    private Integer chargerId;
    private Integer stationId;
    private ChagerType chagerType;
    private ChargerStatus chargerStatus;

    public enum ChagerType {
        SEVEN_KW,  // 7kW
        FIFTY_KW,  // 50kW
        ONE_HUNDRED_KW, // 100kW
        TWO_HUNDRED_KW,  // 200kW
        THREE_HUNDRED_PLUS_KW // 300kW+
    }

    public enum ChargerStatus {
        available, in_use, maintenance
    }
}
