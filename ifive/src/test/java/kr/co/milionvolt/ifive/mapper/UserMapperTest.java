package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.user.UserVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void getListTest() {
        //mapper.getList().stream().forEach(System.out :: println);
        List<UserVO> list = userMapper.getList();

        // 첫번째 사용자
        UserVO firstUser = list.get(0);

        // 예상되는 UserID의 값
        String UserId = "0";
        Assertions.assertEquals(UserId, firstUser.getUserId(), "예상하는 유저ID가 아닙니다.");

        list.stream().map(UserVO::getUserId).forEach(System.out::println);
        list.stream().map(UserVO::getUsername).forEach(System.out::println);
        list.stream().forEach(System.out::println);
        list.forEach(System.out::println);
        System.out.println("getList 완료!!!");
    }

}
