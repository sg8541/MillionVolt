package kr.co.milionvolt.ifive.domain.charger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChargerVO {
    private Integer chargerId;
    private Integer stationId;
    private Integer chargerStatusId;
    private Integer chargerSpeedId;
}
