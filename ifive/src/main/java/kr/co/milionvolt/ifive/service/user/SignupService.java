package kr.co.milionvolt.ifive.service.user;

import kr.co.milionvolt.ifive.domain.user.SignupDTO;

public interface SignupService {

    boolean signup(SignupDTO signupDTO);
    // 이메일 중복 검사
    int checkEmailDuplicate(String email);
    // 아이디 중복 검사
    int checkIdDuplicate(String id);
}
