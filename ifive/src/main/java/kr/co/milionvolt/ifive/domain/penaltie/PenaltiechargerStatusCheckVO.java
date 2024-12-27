package kr.co.milionvolt.ifive.domain.penaltie;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PenaltiechargerStatusCheckVO {
    private Integer chargerId;
    private Integer chargerStatusId;
    private Integer stationId;
}
