package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.notification.ChargingStatusDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChargingStatusMapperTest {
    @Autowired
    private ChargingStatusMapper chargingStatusMapper;

    @Test
    public void test(){
        String userId = "wogjsdl1244";
        int reservationId = 1;
        ChargingStatusDTO dto =  chargingStatusMapper.ChargingStatus(userId,reservationId);
        System.out.println(dto);

    }


}
