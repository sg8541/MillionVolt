package kr.co.milionvolt.ifive.service.chargingstation;

import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationVO;
import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.mapper.ChargingStationMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<ChargingStationVO> getStationsByAddress(String address) {
        if (address == null || address.isBlank()) {
            address = "서울"; // 기본값 설정
        }

        // 주소 키워드 추출
        String keyword = extractAddressKeyword(address);
        System.out.println("주소 키워드로 검색: " + keyword); // 검색 키워드 로그 출력

        List<ChargingStationVO> stations = chargingStationMapper.findStationsByAddress(keyword);
        if (stations.isEmpty()) {
            System.out.println("검색 결과가 없습니다: " + keyword);
        }
        return stations;
    }

    @Override
    public List<ChargingStationVO> getStationsByAddress5km(String address, int radius) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address cannot be null or empty");
        }

        // address를 기반으로 충전소 검색
        List<ChargingStationVO> stations = chargingStationMapper.findStationsByAddress(address);

        // 5km 내 충전소 필터링 (이 로직은 DB 필터링 또는 거리 계산으로 처리 가능)
        return stations.stream()
                .filter(station -> calculateDistance(address, station.getAddress()) <= radius)
                .collect(Collectors.toList());
    }

    // 거리 계산 로직 (예: 두 주소 간의 거리 계산)
    private double calculateDistance(String userAddress, String stationAddress) {
        // Geocoding 또는 다른 방법으로 주소를 위도/경도로 변환하여 거리 계산 (간단한 예시)
        return 5.0; // 5km 내인지 확인하는 로직 구현
    }

    private String extractAddressKeyword(String address) {
        // 간단히 구와 동만 추출
        String[] addressParts = address.split(" ");
        if (addressParts.length >= 3) {
            return addressParts[1] + " " + addressParts[2]; // 예: "송파구 가락동"
        }
        return address.trim();
    }

    @Override
    public ChargingStationDTO getStationWithChargers(Integer stationId) {
        return chargingStationMapper.findStationWithChargers(stationId);
    }

}