package kr.co.milionvolt.ifive.domain.reservation;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoReservationListDTO {
  private Integer reservationId;
  private java.sql.Timestamp startTime;
  private java.sql.Timestamp endTime;
  private Status status;
  private java.sql.Timestamp createdAt;
  private Integer chargerId;
  private String address;

  public enum Status {
    pending,confirmed, cancelled, completed
  }

}
