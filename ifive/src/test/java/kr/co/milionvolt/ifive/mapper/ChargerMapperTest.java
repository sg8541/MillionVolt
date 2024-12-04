//package kr.co.milionvolt.ifive.mapper;
//import kr.co.milionvolt.ifive.domain.ChargerDTO;
//import kr.co.milionvolt.ifive.domain.ChargerVO;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import java.util.List;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class ChargerMapperTest {
//
//    @Autowired
//    private ChargerMapper chargerMapper;
//
//    // 충전기 전체 조회 테스트
//    @Test
//    public void testgetChargers() {
//        // 전체 충전기 목록 조회
//        List<ChargerVO> list = chargerMapper.getChargers();
//
//        // 리스트가 null이 아니어야 하며, 최소한 하나의 충전기가 있어야 한다
//        assertNotNull(list);
//        assertEquals(list.size() > 0, true);
//    }
//
//    // 특정 충전소의 충전기 목록 조회 테스트
//    // 아직 충전소에 대한 db데이터가 없기 때문에 에러 발생.
//    @Test
//    public void testgetChargersByStationId() {
//        Integer stationId = 1; // 예시로 충전소 ID 1을 사용
//
//        // 특정 충전소에 속하는 충전기 목록 조회
//        List<ChargerVO> list = chargerMapper.getChargersByStationId(stationId);
//
//        // 리스트가 null이 아니어야 하며, 충전기 목록이 반환되어야 한다
//        assertNotNull(list);
//        // 예상되는 충전기 개수와 비교
//        assertEquals(list.size() > 0, true);
//    }
//
//    // 특정 충전소의 특정 충전기 상태 조회 테스트
//    // 아직 충전소에 대한 db데이터가 없기 때문에 에러 발생.
//    @Test
//    public void testgetChargerStatusByStationIdAndChargerId() {
//        Integer stationId = 1; // 예시로 충전소 ID 1을 사용
//        Integer chargerId = 1; // 예시로 충전기 ID 1을 사용
//
//        // 특정 충전소의 특정 충전기 상태 조회
//        ChargerDTO.ChargerStatus chargerStatus = chargerMapper.getChargerStatusByStationIdAndChargerId(stationId, chargerId);
//
//        // 상태가 null이 아니어야 한다
//        assertNotNull(chargerStatus);
//        // 상태가 예상된 값인지 확인 (예: "available" 또는 다른 상태)
//        assertEquals(chargerStatus, ChargerDTO.ChargerStatus.available); // 예시로 "available" 상태를 예상
//    }
//
//    // 충전기 상태 변경 테스트
//    // 아직 충전소에 대한 db데이터가 없기 때문에 에러 발생.
//    @Test
//    public void testupdateChargerStatus() {
//        Integer chargerId = 1; // 예시로 충전기 ID 1을 사용
//        ChargerDTO.ChargerStatus newStatus = ChargerDTO.ChargerStatus.in_use; // 상태를 "in_use"로 변경한다고 가정
//
//        // 상태 변경
//        boolean result = chargerMapper.updateChargerStatus(chargerId, newStatus.toString()) > 0;
//
//        // 상태 변경이 성공했는지 확인
//        assertTrue(result);  // 상태 변경이 정상적으로 이루어졌다면 true
//
//        // 상태 변경 후 DB에서 상태가 제대로 업데이트되었는지 확인
//        ChargerDTO.ChargerStatus updatedStatus = chargerMapper.getChargerStatusByStationIdAndChargerId(1, chargerId);
//        System.out.println("Updated status: " + updatedStatus);  // 로그로 상태 확인
//
//        // 상태가 예상한 값("in_use")인지 확인
//        assertEquals(updatedStatus, ChargerDTO.ChargerStatus.in_use);  // expected: in_use, actual: in_use가 나와야함.
//    }
//}
