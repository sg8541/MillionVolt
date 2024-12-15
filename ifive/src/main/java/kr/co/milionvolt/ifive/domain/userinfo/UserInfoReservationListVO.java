package kr.co.milionvolt.ifive.domain.userinfo;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoReservationListVO {
  private Integer reservationId;
  private String startTime;
  private String endTime;
  private Status status;
  private String createdAt;
  private Integer chargerId;
  private String name;

  public enum Status {
    pending,confirmed, cancelled, completed
  }

}
