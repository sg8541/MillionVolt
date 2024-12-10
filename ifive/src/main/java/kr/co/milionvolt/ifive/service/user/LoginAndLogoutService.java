package kr.co.milionvolt.ifive.service.user;

import kr.co.milionvolt.ifive.domain.user.LoginDTO;
import kr.co.milionvolt.ifive.domain.token.TokenResponseDTO;

public interface LoginAndLogoutService {

    public TokenResponseDTO authenticate(LoginDTO loginDTO);
    void logout(String refreshToken);
    TokenResponseDTO refreshToken(String refreshToken);
    void saveRefreshToken(String newRefreshToken, Integer id);
}
