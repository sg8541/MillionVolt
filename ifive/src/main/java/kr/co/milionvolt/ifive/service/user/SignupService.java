package kr.co.milionvolt.ifive.service.user;

import kr.co.milionvolt.ifive.domain.user.SignupDTO;

public interface SignupService {

    boolean signup(SignupDTO signupDTO);
}
