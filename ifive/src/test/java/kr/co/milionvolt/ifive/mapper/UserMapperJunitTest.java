package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.user.FindFwdDTO;
import kr.co.milionvolt.ifive.domain.user.FindIdDTO;
import kr.co.milionvolt.ifive.domain.user.ResetDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMapperJunitTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void findById(){ // 아이디 찾기 기능 Test
        FindIdDTO dto =  userMapper.findById("강재헌","wogjsdl1244@naver.com");
        System.out.println(dto);
       System.out.println(dto.getUserId());
        System.out.println(dto.getCreatedAt());
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
        FindFwdDTO vo = userMapper.findPassword(username,email);

        System.out.println(vo);

    }

    @Test
    public void newPassword(){
        ResetDTO vo = new ResetDTO();
        vo.setPassword("9999");
        vo.setUserId("wogjsdl1244");
        vo.setUsername("강재헌");
        vo.setEmail("wogjsdl1244@naver.com");

        userMapper.newPassword(vo);



    }
}

