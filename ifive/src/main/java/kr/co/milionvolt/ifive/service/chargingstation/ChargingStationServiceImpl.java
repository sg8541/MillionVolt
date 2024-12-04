package kr.co.milionvolt.ifive.service.chargingstation;

import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationVO;
import kr.co.milionvolt.ifive.mapper.ChargingStationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChargingStationServiceImpl implements ChargingStationService{
    @Autowired
    private ChargingStationMapper chargingStationMapper;

    // 전체 충전소 조회(충전기 타입과 충전기 상태 포함)
    public List<ChargingStationVO> getAllChargingStations() {
        return chargingStationMapper.getAllChargingStationList();
    }

    // 충전소 개별 조회
    public List<ChargingStationVO> getChargingStationById(Integer stationId) {
        return chargingStationMapper.getOneChargingStationList(stationId);
    }

    // 충전소 수정
    public boolean updateChargingStation(ChargingStationDTO chargingStationDTO) {
        int updatedRows = chargingStationMapper.updateChargingStation(chargingStationDTO);
        return updatedRows > 0;
    }

    // 충전기 정보 수정 (충전기 상태, 충전기 타입)
    public boolean updateChargerInfo(Integer chargerId, ChargerDTO.ChagerType chagerType, ChargerDTO.ChargerStatus chargerStatus) {
        int updatedRows = chargingStationMapper.updateChargerInfo(chargerId, chagerType, chargerStatus);
        return updatedRows > 0;
    }
}
