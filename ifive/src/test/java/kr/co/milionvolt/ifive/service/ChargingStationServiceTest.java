package kr.co.milionvolt.ifive.service;

import kr.co.milionvolt.ifive.domain.ChargingStationDTO;
import kr.co.milionvolt.ifive.domain.ChargingStationVO;
import kr.co.milionvolt.ifive.domain.ChargerDTO;
import kr.co.milionvolt.ifive.mapper.ChargingStationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.Mock;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ChargingStationServiceTest {
    @Mock
    private ChargingStationMapper chargingStationMapper;

    @InjectMocks
    private ChargingStationService chargingStationService = new ChargingStationServiceImpl();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Mock 객체 초기화
    }

    // 전체 충전소 조회(충전기 타입과 충전기 상태 포함)
    @Test
    public void testGetAllChargingStations() {
        // Given: Mock 충전소 목록
        List<ChargingStationVO> mockStations = new ArrayList<>();
        mockStations.add(new ChargingStationVO(1, "Station 1", "서울특별시 강남구 테헤란로 123", 10, 5, 2, 3, 3, 2, 1,
                ChargingStationVO.ChargeSpeed.fast, new BigDecimal("0.15"), null, ChargingStationVO.ChagerType.FIFTY_KW, ChargingStationVO.ChargerStatus.available));
        mockStations.add(new ChargingStationVO(2, "Station 2", "부산광역시 해운대구 마린시티 456", 15, 8, 4, 5, 4, 2, 3,
                ChargingStationVO.ChargeSpeed.medium, new BigDecimal("0.20"), null, ChargingStationVO.ChagerType.ONE_HUNDRED_KW, ChargingStationVO.ChargerStatus.in_use));

        // When: 전체 충전소 목록 조회
        when(chargingStationMapper.getAllChargingStationList()).thenReturn(mockStations);

        // Then: 서비스 메소드 호출 및 검증
        List<ChargingStationVO> result = chargingStationService.getAllChargingStations();

        // 결과가 예상과 일치하는지 확인
        assertEquals(2, result.size(), "충전소 목록의 크기가 예상과 다릅니다."); // 두 개의 충전소가 반환되어야 한다.

        // 첫 번째 충전소 정보 검증
        assertEquals("Station 1", result.get(0).getName(), "첫 번째 충전소 이름이 예상과 다릅니다.");
        assertEquals("서울특별시 강남구 테헤란로 123", result.get(0).getAddress(), "첫 번째 충전소 주소가 예상과 다릅니다.");
        assertEquals(10, result.get(0).getTotalCharger(), "첫 번째 충전소의 총 충전기 수가 예상과 다릅니다.");

        // 두 번째 충전소 정보 검증
        assertEquals("Station 2", result.get(1).getName(), "두 번째 충전소 이름이 예상과 다릅니다.");
        assertEquals("부산광역시 해운대구 마린시티 456", result.get(1).getAddress(), "두 번째 충전소 주소가 예상과 다릅니다.");
        assertEquals(15, result.get(1).getTotalCharger(), "두 번째 충전소의 총 충전기 수가 예상과 다릅니다.");
    }

    // 충전소 개별 조회
    @Test
    public void testGetChargingStationById() {
        // Given: Mock 충전소 데이터 (빈 리스트 대신 실제 테스트용 데이터 생성)
        ChargingStationVO mockStation = new ChargingStationVO(1, "서울 충전소", "서울특별시 강남구 테헤란로 123", 10, 5, 2, 3, 3, 2, 1,
                ChargingStationVO.ChargeSpeed.fast, new BigDecimal("0.15"), null, ChargingStationVO.ChagerType.FIFTY_KW, ChargingStationVO.ChargerStatus.available);
        List<ChargingStationVO> mockStations = new ArrayList<>();
        mockStations.add(mockStation);

        // When: 충전소 목록 조회
        when(chargingStationMapper.getOneChargingStationList(1)).thenReturn(mockStations);

        // Then: 서비스 메소드 호출 및 검증
        List<ChargingStationVO> result = chargingStationService.getChargingStationById(1);

        // 결과가 예상과 일치하는지 확인
        assertEquals(1, result.size(), "충전소 목록의 크기가 예상과 다릅니다."); // 충전소 1개가 반환되어야 한다.
        assertEquals("서울 충전소", result.get(0).getName(), "충전소 이름이 예상과 다릅니다."); // 충전소 이름이 "Station 1"이어야 한다.
        assertEquals("서울특별시 강남구 테헤란로 123", result.get(0).getAddress(), "충전소 주소가 예상과 다릅니다."); // 주소 확인
        assertEquals(10, result.get(0).getTotalCharger(), "충전기 수가 예상과 다릅니다."); // 총 충전기 수 확인
    }

    // 충전소 수정
    @Test
    public void testUpdateChargingStation() {
        // Given: 수정할 충전소 DTO
        ChargingStationDTO chargingStationDTO = new ChargingStationDTO(
                1, "Updated Station", "Updated Address", 12, 8, 3, 4, 4, 2, 1,
                ChargingStationDTO.ChargeSpeed.fast, new BigDecimal("0.15"), null, null,
                ChargingStationDTO.ChagerType.FIFTY_KW, ChargingStationDTO.ChargerStatus.available
        );

        // When: 충전소 업데이트가 정상적으로 이루어짐
        when(chargingStationMapper.updateChargingStation(chargingStationDTO)).thenReturn(1); // 1은 업데이트 성공을 의미

        // Then: 서비스 메소드 호출 및 검증
        boolean result = chargingStationService.updateChargingStation(chargingStationDTO);

        // 수정이 성공했으면 true 반환
        assertTrue(result);
    }


    // 충전기 정보 수정 (충전기 상태, 충전기 타입)
    @Test
    public void testUpdateChargerInfo() {
        // Given: 수정할 충전기 정보
        Integer chargerId = 1;
        ChargerDTO.ChagerType newChagerType = ChargerDTO.ChagerType.ONE_HUNDRED_KW;
        ChargerDTO.ChargerStatus newChargerStatus = ChargerDTO.ChargerStatus.maintenance;

        // When: 충전기 정보 업데이트가 정상적으로 이루어짐
        when(chargingStationMapper.updateChargerInfo(chargerId, newChagerType, newChargerStatus)).thenReturn(1); // 1은 업데이트 성공을 의미

        // Then: 서비스 메소드 호출 및 검증
        boolean result = chargingStationService.updateChargerInfo(chargerId, newChagerType, newChargerStatus);
        assert result; // 수정이 성공했으면 true 반환
    }

    // 충전소 필터링

    // 검색
}
