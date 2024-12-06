package kr.co.milionvolt.ifive.service;

import kr.co.milionvolt.ifive.domain.ChargerDTO;
import kr.co.milionvolt.ifive.domain.ChargerVO;
import kr.co.milionvolt.ifive.mapper.ChargerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ChargerServiceTest {

    @InjectMocks
    private ChargerServiceImpl chargerService; // 테스트할 서비스 클래스

    @Mock
    private ChargerMapper chargerMapper; // ChargerMapper를 Mock 객체로 주입

    // 테스트 데이터
    private ChargerVO chargerVO;
    private ChargerDTO.ChargerStatus chargerStatus;

    @BeforeEach
    public void setUp() {
        // 테스트용 데이터 준비
        chargerVO = new ChargerVO(1, 101, ChargerVO.ChagerType.SEVEN_KW, ChargerVO.ChargerStatus.available);
        chargerStatus = ChargerDTO.ChargerStatus.available;
    }

    // 충전기 전체 조회 테스트
    @Test
    public void testGetChargers() {
        // Mocking chargerMapper의 getChargers() 메서드
        when(chargerMapper.getChargers()).thenReturn(Arrays.asList(chargerVO));

        // 실제 서비스 메서드 호출
        List<ChargerVO> chargers = chargerService.getChargers();

        // 결과 확인
        assertNotNull(chargers);
        assertEquals(1, chargers.size());
        assertEquals(chargerVO.getChargerId(), chargers.get(0).getChargerId());
    }

    // 특정 충전소의 충전기 목록 조회 테스트
    @Test
    public void testGetChargersByStationId() {
        int stationId = 101;
        when(chargerMapper.getChargersByStationId(stationId)).thenReturn(Arrays.asList(chargerVO));

        List<ChargerVO> chargers = chargerService.getChargersByStationId(stationId);

        assertNotNull(chargers);
        assertEquals(1, chargers.size());
        assertEquals(stationId, chargers.get(0).getStationId());
    }

    // 특정 충전소의 특정 충전기 상태 조회 테스트
    @Test
    public void testGetChargerStatusByStationIdAndChargerId() {
        int stationId = 101;
        int chargerId = 1;

        // Mocking chargerMapper의 getChargerStatusByStationIdAndChargerId
        when(chargerMapper.getChargerStatusByStationIdAndChargerId(stationId, chargerId)).thenReturn(chargerStatus);

        ChargerDTO.ChargerStatus status = chargerService.getChargerStatusByStationIdAndChargerId(stationId, chargerId);

        assertNotNull(status);
        assertEquals(chargerStatus, status);
    }

    // 충전기 상태 변경 테스트
    @Test
    public void testUpdateChargerStatus() {
        int chargerId = 1;
        String status = "in_use";

        // Mocking chargerMapper의 updateChargerStatus
        when(chargerMapper.updateChargerStatus(chargerId, status)).thenReturn(1);

        boolean result = chargerService.updateChargerStatus(chargerId, status);

        assertTrue(result);
    }

    // 충전기 상태 변경 실패 테스트
    @Test
    public void testUpdateChargerStatusFailure() {
        int chargerId = 1;
        String status = "in_use";

        // Mocking chargerMapper의 updateChargerStatus 메서드 실패시
        when(chargerMapper.updateChargerStatus(chargerId, status)).thenReturn(0);

        boolean result = chargerService.updateChargerStatus(chargerId, status);

        assertFalse(result);
    }
}
