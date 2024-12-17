package kr.co.milionvolt.ifive.service.chargingstation;

import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationVO;
import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;

import java.util.List;

public interface ChargingStationService {

    // 모든 충전소 조회
    List<ChargingStationVO> getAllChargingStations(int page, int size);

    // 단일 충전소 조회 (모달창)
    ChargingStationVO getChargingStationById(Integer stationId);

    // 충전소 등록
    void insertChargingStation(ChargingStationDTO station);

    // 검색 및 필터링 결과
    List<ChargingStationVO> filterByChargeSpeed(Integer chargerSpeedId, String address);

    // 충전소 검색
    List<ChargingStationVO> searchChargingStations(String query, int page, int size);

    // CSV 데이터를 DB에 저장하는 메서드
    void saveStationsToDb(List<ChargingStationDTO> stations);

    // 사용자 주변 충전소 목록 조회
    List<ChargingStationVO> getNearbyStations(String address, Integer charger_speed_id);

    // 주변 충전소 상태 조회 (사이드바)
    List<ChargingStationVO> getSidebarStations(String address);

    ChargingStationDTO getChargingStationWithChargers(Integer stationId);

    // 사용자 주변의 충전소 가져오기
    List<ChargingStationVO> getStationsByAddress5km(String address, int radius);

    List<ChargingStationVO> getStationsByAddress(String address);

    ChargingStationDTO getStationWithChargers(Integer stationId);
}