package kr.co.milionvolt.ifive.service.chargingstation;

import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationVO;

import java.util.List;

public interface ChargingStationService {

    // 전체 충전소 조회(충전기 타입과 충전기 상태 포함)
    List<ChargingStationVO> getAllChargingStations();

    // 충전소 개별 조회
    List<ChargingStationVO> getChargingStationById(Integer stationId);

    // 충전소 수정
    boolean updateChargingStation(ChargingStationDTO chargingStationDTO);

    // 충전기 정보 수정 (충전기 상태, 충전기 타입)
    boolean updateChargerInfo(Integer stationId, ChargerDTO.ChagerType chagerType, ChargerDTO.ChargerStatus chargerStatus);

}
