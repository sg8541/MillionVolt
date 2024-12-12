package kr.co.milionvolt.ifive.domain.charger;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChargerDTO {
  private Integer chargerId;
  private Integer stationId;
  private Integer chargerStatusId;
  private Integer chargerSpeedId;
}
