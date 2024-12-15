package kr.co.milionvolt.ifive.service.charger;

import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.domain.charger.ChargerVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChargerService {

    // 1. 충전소의 모든 충전기 조회
    List<ChargerDTO> getChargersByStationId(Integer stationId);

    // 2. 특정 충전기의 상태 조회
    ChargerVO getChargerById(Integer chargerId);

    // 3. 충전기 상태 변경 (예약 가능/불가능 등)
    boolean updateChargerStatus(Integer chargerId, String status);

    // 4. 특정 충전소의 예약 가능한 충전기 조회
    List<ChargerVO> getAvailableChargersByStationId(Integer stationId);

    // 5. 충전기 상태에 따른 필터링
    List<ChargerVO> getChargersByStatus(String status);
}
