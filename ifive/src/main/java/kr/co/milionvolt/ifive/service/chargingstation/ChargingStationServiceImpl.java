package kr.co.milionvolt.ifive.service.chargingstation;

import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationVO;
import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.mapper.ChargingStationMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChargingStationServiceImpl implements ChargingStationService {

    private final ChargingStationMapper chargingStationMapper;

    public ChargingStationServiceImpl(ChargingStationMapper chargingStationMapper) {
        this.chargingStationMapper = chargingStationMapper;
    }
    @Override
    public void saveStationsToDb(List<ChargingStationDTO> stations) {
        for (ChargingStationDTO dto : stations) {
            insertChargingStation(dto); // 변환된 VO를 insert 메서드에 전달
        }
    }

    @Override
    public void insertChargingStation(ChargingStationDTO station) {
        chargingStationMapper.insertChargingStation(station);
    }

    private ChargingStationVO convertDtoToVo(ChargingStationDTO dto) {
        return new ChargingStationVO(
                dto.getStationId(),
                dto.getName(),
                dto.getAddress(),
                dto.getTotalCharger(),
                dto.getAvailableCharger(),
                dto.getChargerSpeedId(),
                dto.getPricePerKWh(),
                dto.getFilePath(),
                dto.getFacilityType(),
                dto.getDeviceType(),
                dto.getChargerType(),
                dto.getChargerStatusId(),
                dto.getChargers()
        );
    }

    @Override
    public List<ChargingStationVO> getAllChargingStations(int page, int size) {
        int offset = (page - 1) * size;
        return chargingStationMapper.getAllChargingStations(offset, size);
    }

    @Override
    public List<ChargingStationVO> getNearbyStations(String address, Integer charger_speed_id) {
        return chargingStationMapper.getSidebarStations(address); // address 기반으로 조회
    }

    @Override
    public ChargingStationVO getChargingStationById(Integer stationId) {
        return chargingStationMapper.getChargingStationById(stationId);
    }

    @Override
    public List<ChargingStationVO> searchChargingStations(String query, int page, int size) {
        int offset = (page - 1) * size;
        return chargingStationMapper.searchChargingStations(query, offset, size);
    }

    @Override
    public List<ChargingStationVO> getSidebarStations(String address) {
        return chargingStationMapper.getSidebarStations(address);
    }

    @Override
    public List<ChargingStationVO> filterByChargeSpeed(Integer chargerSpeedId, String address) {
        return chargingStationMapper.findStationsByChargeSpeed(chargerSpeedId, address);
    }

    @Override
    public ChargingStationDTO getChargingStationWithChargers(Integer stationId) {
        // 충전소 정보 조회
        ChargingStationDTO station = chargingStationMapper.findStationById(stationId);

        // 충전소에 속한 충전기 리스트 조회
        List<ChargerDTO> chargers = chargingStationMapper.findChargersByStationId(stationId);
        station.setChargers(chargers); // 충전소에 충전기 리스트 추가

        return station;
    }

    @Override
    public List<ChargingStationVO> getStationsByLocation(double latitude, double longitude, int radius) {
        return chargingStationMapper.findNearbyStations(latitude, longitude, radius);
    }

    @Override
    public ChargingStationDTO getStationWithChargers(Integer stationId) {
        return chargingStationMapper.findStationWithChargers(stationId);
    }

}
