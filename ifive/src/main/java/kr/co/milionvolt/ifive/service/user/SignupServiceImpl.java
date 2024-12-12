package kr.co.milionvolt.ifive.service.user;

import kr.co.milionvolt.ifive.domain.user.SignupDTO;
import kr.co.milionvolt.ifive.mapper.SignupMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SignupServiceImpl implements SignupService {

    private final PasswordEncoder passwordEncoder;
    private final SignupMapper signupMapper;
    private final SqlSessionFactory sqlSessionFactory;

    @Override
    public boolean signup(SignupDTO signupDTO) throws DuplicateKeyException {
        signupDTO.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        log.info("signup : {}", signupDTO);
        SqlSession sqlSession = sqlSessionFactory.openSession(false);
        try {
            boolean insertUserResult = signupMapper.insertUser(signupDTO);
            if (insertUserResult) {
                String carId = signupMapper.findById(signupDTO.getEmail());
                boolean insertUserCarResult = signupMapper.insertUserCar(carId, signupDTO.getCarNumber(), signupDTO.getChargerSpeedId(), signupDTO.getModelId(), signupDTO.getCarBattery());
            } else {
                sqlSession.rollback();
                throw new DuplicateKeyException("이미 사용중인 이메일 또는 아이디입니다.");
            }
            return true;
        } catch (DuplicateKeyException e) {
            sqlSession.rollback();
            throw e;
        } finally {
            sqlSession.close();
        }
    }
}
