package kr.co.milionvolt.ifive.domain.user;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SignupDTO {
  private String username;
  private String userId;
  private String email;
  private String phoneNumber;
  private String password;
}
