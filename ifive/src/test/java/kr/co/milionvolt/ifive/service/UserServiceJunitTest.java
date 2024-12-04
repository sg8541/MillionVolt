package kr.co.milionvolt.ifive.service;

import kr.co.milionvolt.ifive.dto.UserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceJunitTest {
    @Autowired
    private UserService userService;

    @Test
    public void findbyId(){
        String username ="강재헌";
        String email = "wogjsdl1244@naver.com";
        UserVO vo = userService.findByID(username,email);
        System.out.println(vo);
    }

    @Test
    public void findPasswordByUserId(){
        String userId = "wogjsdl1244";
        String userId2 = "wogjsdl1234";
        String email = userService.findPasswordByUserId(userId);
        System.out.println(email);
    }

    @Test
    public void findPwd(){
        UserVO vo = new UserVO();
        vo.setPassword("1244");
        vo.setUser_id("wogjsdl1244");
        vo.setUsername("강재헌");
        vo.setEmail("wogjsdl1244@naver.com");

        userService.newPwd(vo);

    }
}
