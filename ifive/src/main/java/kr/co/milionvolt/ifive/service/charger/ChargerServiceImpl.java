package kr.co.milionvolt.ifive.service.charger;

import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.domain.charger.ChargerVO;
import kr.co.milionvolt.ifive.mapper.ChargerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChargerServiceImpl implements ChargerService{
    @Autowired
    private ChargerMapper chargerMapper;

    // 충전기 전체 조회
    public List<ChargerVO> getChargers(){
        return chargerMapper.getChargers();
    }

    // 특정 충전소의 충전기 목록 조회
    public List<ChargerVO> getChargersByStationId(Integer stationId) {
        return chargerMapper.getChargersByStationId(stationId);
    }

    // 특정 충전소의 특정 충전기의 상태 조회
    public ChargerDTO.ChargerStatus getChargerStatusByStationIdAndChargerId(Integer stationId, Integer chargerId) {
        return chargerMapper.getChargerStatusByStationIdAndChargerId(stationId, chargerId);
    }

    // 충전기 상태 변경
    public boolean updateChargerStatus(Integer chargerId, String status) {
        int result = chargerMapper.updateChargerStatus(chargerId, status);
        return result > 0; // 0보다 크면 상태가 변경된 것
    }

    // 충전기 상태 필터링
    public List<ChargerVO> getChargersByStatus(ChargerDTO.ChargerStatus status) {
        return chargerMapper.getChargersByStatus(status);
    }
}
