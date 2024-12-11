package kr.co.milionvolt.ifive.domain.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsVO {
    private Integer id;
    private String password;
    private String userId;
    private String role;
    


}
