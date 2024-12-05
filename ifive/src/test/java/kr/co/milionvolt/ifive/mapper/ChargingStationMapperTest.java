package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ChargingStationMapperTest {

    @Autowired
    private ChargingStationMapper chargingStationMapper;

    @Test
    public void getAllChargingStationList() {
        // 모든 충전소 리스트 조회
        List<ChargingStationVO> chargingStationList = chargingStationMapper.getAllChargingStationList();

        // 결과가 null이 아니어야 하고, 적어도 1개의 충전소가 있어야 한다
        assertNotNull(chargingStationList, "충전소 리스트가 null입니다.");
        assertTrue(chargingStationList.size() > 0, "충전소 리스트가 비어 있습니다.");
    }

    @Test
    public void getOneChargingStationList() {
        Integer stationId = 1;  // 조회할 충전소 ID (예시: 1번)

        // 특정 충전소 조회
        List<ChargingStationVO> chargingStation = chargingStationMapper.getOneChargingStationList(stationId);

        // 결과가 null이 아니어야 한다
        assertNotNull(chargingStation, "충전소 정보가 null입니다.");
        // 조회된 충전소 목록의 크기가 1 이상이어야 한다
        assertTrue(chargingStation.size() > 0, "충전소 정보가 없습니다.");
    }
}
