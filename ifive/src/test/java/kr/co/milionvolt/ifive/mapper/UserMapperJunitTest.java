package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.UserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMapperJunitTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void findById(){ // 아이디 찾기 기능 Test
       UserVO vo =  userMapper.findById("강재헌","wogjsdl1244@naver.com");
        System.out.println(vo);
       System.out.println(vo.getUserId());
        System.out.println(vo.getCreatedAt());
    }

    @Test
    public void findPasswordByUserId(){ // 비밀번호 찾기 전 해당하는 아이디가 존재하는지 확인.
        String email = userMapper.findPasswordByUserId("wogjsdl1244");
        System.out.println(email);
    }

    @Test
    public void findPwd(){
        String username = "강재헌";
        String email = "wogjsdl1244@naver.com";
        UserVO vo = userMapper.findPassword(username,email);

        System.out.println(vo);

    }

//    @Test
//    public void newPassword(){
//        UserVO vo = new UserVO();
//        vo.setPassword("1111");
//        vo.setUser_id("wogjsdl1244");
//        vo.setUsername("강재헌");
//        vo.setEmail("wogjsdl1244@naver.com");
//
//        userMapper.newPassword(vo);
//
//
//
//    }
}

