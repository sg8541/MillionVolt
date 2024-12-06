package kr.co.milionvolt.ifive.service.charger;
import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.domain.charger.ChargerVO;

import java.util.List;

public interface ChargerService {

    // 충전기 전체 조회
    List<ChargerVO> getChargers();

    // 특정 충전소의 전체 충전기 목록 조회
    List<ChargerVO> getChargersByStationId(Integer stationId);

    // 특정 충전소의 특정 충전기의 상태 조회
    ChargerDTO.ChargerStatus getChargerStatusByStationIdAndChargerId(Integer stationId, Integer chargerId);

    // 충전기 상태 변경
    boolean updateChargerStatus(Integer chargerId, String status);

    // 충전기 상태 필터링
    List<ChargerVO> getChargersByStatus(ChargerDTO.ChargerStatus status);
}
