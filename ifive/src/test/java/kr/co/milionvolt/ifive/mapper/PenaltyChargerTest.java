package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.penaltie.PenaltiechargerStatusCheckVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class PenaltyChargerTest {
    @Autowired
    private PenaltyMapper penaltyMapper;

//    @Test
//    public void test(){
//       PenaltiechargerStatusCheckVO  vo =  penaltyMapper.findChargerId(12);
//        System.out.println(vo);
//    }

//    @Test
//    public void test2(){
//        LocalDateTime now = LocalDateTime.now();
//       LocalDateTime localDateTime= penaltyMapper.findCloseStartTime(now,2);
//        System.out.println(localDateTime);
//    }

}
