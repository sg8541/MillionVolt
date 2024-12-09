package kr.co.milionvolt.ifive.service.chargingstation;

import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationVO;
import kr.co.milionvolt.ifive.mapper.ChargingStationMapper;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ChargingStationServiceImpl implements ChargingStationService {

    private final ChargingStationMapper chargingStationMapper;

    public ChargingStationServiceImpl(ChargingStationMapper chargingStationMapper) {
        this.chargingStationMapper = chargingStationMapper;
    }

    @Override
    public List<ChargingStationVO> getAllChargingStations() {
        return chargingStationMapper.getAllChargingStationList();
    }

    @Override
    public List<ChargingStationVO> getChargingStationById(Integer stationId) {
        ChargingStationVO station = chargingStationMapper.getOneChargingStation(stationId);
        return station != null ? List.of(station) : List.of();
    }

    @Override
    public boolean updateChargingStation(ChargingStationDTO chargingStationDTO) {
        int updatedRows = chargingStationMapper.updateChargingStation(chargingStationDTO);
        return updatedRows > 0;
    }

    @Override
    public boolean updateChargerInfo(Integer chargerId, ChargerDTO.ChagerType chagerType, ChargerDTO.ChargerStatus chargerStatus) {
        int updatedRows = chargingStationMapper.updateChargerInfo(chargerId, chagerType, chargerStatus);
        return updatedRows > 0;
    }

    @Override
    public List<ChargingStationVO> getChargingStationsByFilter(ChargingStationDTO.ChargeSpeed chargeSpeed, ChargerDTO.ChargerStatus chargerStatus) {
        return chargingStationMapper.getChargingStationsByFilter(chargeSpeed, chargerStatus);
    }

    @Override
    public List<ChargingStationVO> getChargingStationsByLocation(String address, ChargingStationDTO.ChargeSpeed chargeSpeed, ChargerDTO.ChargerStatus chargerStatus) {
        return chargingStationMapper.getChargingStationsByLocation(
                address,
                chargeSpeed != null ? chargeSpeed.name() : null,
                chargerStatus != null ? chargerStatus.name() : null
        );
    }

    @Override
    public List<ChargingStationVO> searchChargingStations(String query) {
        return chargingStationMapper.searchChargingStations(query);
    }

}