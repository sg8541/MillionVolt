package kr.co.milionvolt.ifive.domain.userinfo;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoReservationListVO {
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
