package kr.co.milionvolt.ifive.domain;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

  private Integer notificationId;
  private String message;
  private Status status;
  private java.sql.Timestamp createdAt;

  public enum Status {
    unread, read
  }

}
