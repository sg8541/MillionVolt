package kr.co.milionvolt.ifive.service.chargingstation;

import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationVO;

import java.util.List;

public interface ChargingStationService {

    List<ChargingStationVO> getAllChargingStations();

    List<ChargingStationVO> getChargingStationById(Integer stationId);

    boolean updateChargingStation(ChargingStationDTO chargingStationDTO);

    boolean updateChargerInfo(Integer chargerId, ChargerDTO.ChagerType ChagerType, ChargerDTO.ChargerStatus chargerStatus);

    List<ChargingStationVO> getChargingStationsByFilter(ChargingStationDTO.ChargeSpeed chargeSpeed, ChargerDTO.ChargerStatus chargerStatus);

    List<ChargingStationVO> getChargingStationsByLocation(String address, ChargingStationDTO.ChargeSpeed chargeSpeed, ChargerDTO.ChargerStatus chargerStatus);

    List<ChargingStationVO> searchChargingStations(String query);

}
