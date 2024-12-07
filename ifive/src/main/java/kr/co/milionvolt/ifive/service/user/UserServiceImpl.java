package kr.co.milionvolt.ifive.service.user;

import kr.co.milionvolt.ifive.mapper.UserMapper;
import kr.co.milionvolt.ifive.domain.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 유저 전체 조회
    @Override
    public List<UserVO> findAll() {
        List<UserVO> vo = new ArrayList<>();
        return vo;
    }
}
