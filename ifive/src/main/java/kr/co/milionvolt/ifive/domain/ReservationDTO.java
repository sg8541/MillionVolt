package kr.co.milionvolt.ifive.domain;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {

  private Integer reservationId;
  private Integer chargerId;
  private java.sql.Timestamp startTime;
  private java.sql.Timestamp endTime;
  private Status status;
  private java.sql.Timestamp createdAt;

  public enum Status {
    pending,confirmed, cancelled, completed
  }

}
