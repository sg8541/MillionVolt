package kr.co.milionvolt.ifive.domain.user;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
  private Integer id;
  private String username;
  private String userId;
  private String email;
  private String phoneNumber;
  private String password;
  private Integer levelId;
  private java.sql.Timestamp createdAt;

}
