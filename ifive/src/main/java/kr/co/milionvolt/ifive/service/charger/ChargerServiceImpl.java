package kr.co.milionvolt.ifive.service.charger;

import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.domain.charger.ChargerVO;
import kr.co.milionvolt.ifive.mapper.ChargerMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChargerServiceImpl implements ChargerService {

    private final ChargerMapper chargerMapper;

    public ChargerServiceImpl(ChargerMapper chargerMapper) {
        this.chargerMapper = chargerMapper;
    }

    // 1. 특정 충전소의 모든 충전기 조회
    @Override
    public List<ChargerDTO> getChargersByStationId(Integer stationId) {
        return chargerMapper.getChargersByStationId(stationId);
    }

    // 2. 특정 충전기의 상태 조회
    @Override
    public ChargerVO getChargerById(Integer stationId, Integer chargerId) {
        return chargerMapper.getChargerById(stationId, chargerId);
    }

    // 3. 충전기 상태 변경
    @Override
    public boolean updateChargerStatus(Integer stationId, Integer chargerId, Integer status) {
        int updatedRows = chargerMapper.updateChargerStatus(stationId, chargerId, status);
        return updatedRows > 0;
    }

    // 4. 특정 충전소의 예약 가능한 충전기 조회
    @Override
    public List<ChargerVO> getAvailableChargersByStationId(Integer stationId) {
        return chargerMapper.getAvailableChargersByStationId(stationId);
    }

    // 5. 충전기 상태에 따른 필터링
    @Override
    public List<ChargerVO> getChargersByStatus(Integer status) {
        return chargerMapper.getChargersByStatus(status);
    }

    @Override
    public List<Map<String, Object>> getChargersWithDetails(Integer stationId) {
        return chargerMapper.findChargersWithDetails(stationId);
    }



}