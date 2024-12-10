package kr.co.milionvolt.ifive.domain.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponseDTO {
    private Integer id;
    private String userId;
    private String role;
    private String accessToken;
    private String refreshToken;
}