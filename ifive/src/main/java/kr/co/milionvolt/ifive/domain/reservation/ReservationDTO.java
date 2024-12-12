package kr.co.milionvolt.ifive.domain.reservation;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {

  private Integer reservationId;
  private Integer chargerId;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private Status status;
  private LocalDateTime createdAt;
  private Integer userId;
  private Integer stationId;
  private String impUid;

  public enum Status {
    pending, confirmed, cancelled, completed
  }
}
