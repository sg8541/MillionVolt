package kr.co.milionvolt.ifive.service;

import kr.co.milionvolt.ifive.mapper.UserMapper;
import kr.co.milionvolt.ifive.domain.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    // private final BCryptPasswordEncoder bCryptPasswordEncoder;
    // 시큐리티 적용 시 사용.


    // 유저 전체 조회
    @Override
    public List<UserVO> findAll() {
        List<UserVO> vo = new ArrayList<>();
        return vo;
    }

    @Override
    public UserVO findByID(String username, String email) {
       UserVO vo = userMapper.findById(username, email);
       return vo;
    }

    @Override
    public String findPasswordByUserId(String userId) {
        String  email = userMapper.findPasswordByUserId(userId);
        return email;
    }




    @Override
    public UserVO findPass(String username, String email) {
        UserVO vo =  userMapper.findPassword(username,email);
        return vo;
    }

    @Override
    public void newPwd(UserVO vo) {
        // vo.setPassword(bCryptPasswordEncoder.encode(vo.getPassword()));
        userMapper.newPassword(vo);
    }
}
