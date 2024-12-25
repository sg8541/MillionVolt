package kr.co.milionvolt.ifive.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PenaltyMapperTest {

    @Autowired
    private PenaltyMapper penaltyMapper;

    @Test
    public void test(){
      int num =   penaltyMapper.selectPenatlyAmount(13);
        System.out.println(num);
    }
}
