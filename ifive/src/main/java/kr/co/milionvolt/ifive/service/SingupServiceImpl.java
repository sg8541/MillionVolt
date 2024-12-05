package kr.co.milionvolt.ifive.service;

import kr.co.milionvolt.ifive.dto.user.SignupDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mybatis.dao.SignupMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SingupServiceImpl implements SingupService {

    private final SignupMapper signupMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean singup(SignupDTO signup) {
        signup.setPassword(passwordEncoder.encode(signup.getPassword()));
        log.info("Signup : {}", signup);
        boolean result = signupMapper.insertUser(signup);
        return result;
    }
}
