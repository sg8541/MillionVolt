package kr.co.milionvolt.ifive.service.user;

import kr.co.milionvolt.ifive.domain.user.SignupDTO;
import kr.co.milionvolt.ifive.mapper.SignupMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SignupServiceImpl implements SignupService {

    private final PasswordEncoder passwordEncoder;
    private final SignupMapper signupMapper;

    @Override
    public boolean signup(SignupDTO signupDTO) {
        signupDTO.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        boolean result = signupMapper.insertUser(signupDTO);
        log.info("signup : {}", signupDTO);
        return result;
    }
}
