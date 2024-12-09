package kr.co.milionvolt.ifive.service;

import kr.co.milionvolt.ifive.domain.notification.ChargingStatusDTO;
import kr.co.milionvolt.ifive.service.charging.ChargingStatusSerivce;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChargingStatusService {
    @Autowired
    private ChargingStatusSerivce chargingStatusSerivce;

    @Test
    public void test(){
        String userId ="wogjsdl1244";
        int reservationId =1;
        ChargingStatusDTO dto =  chargingStatusSerivce.chargingStatus(userId,reservationId);
        System.out.println(dto);

    }
}
