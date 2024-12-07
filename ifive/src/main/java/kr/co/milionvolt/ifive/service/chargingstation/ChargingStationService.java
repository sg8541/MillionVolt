package kr.co.milionvolt.ifive.service.chargingstation;

import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationVO;

import java.util.List;

public interface ChargingStationService {

    /**
     * 전체 충전소 조회 (충전기 타입과 상태 포함)
     */
    List<ChargingStationVO> getAllChargingStations();

    /**
     * 충전소 개별 조회
     * @param stationId 충전소 ID
     */
    List<ChargingStationVO> getChargingStationById(Integer stationId);

    /**
     * 충전소 정보 수정
     * @param chargingStationDTO 수정할 충전소 정보
     */
    boolean updateChargingStation(ChargingStationDTO chargingStationDTO);

    /**
     * 충전기 정보 수정 (충전기 상태, 충전기 타입)
     * @param stationId 충전소 ID
     * @param chagerType 충전기 타입
     * @param chargerStatus 충전기 상태
     */
    boolean updateChargerInfo(Integer stationId, ChargerDTO.ChagerType chagerType, ChargerDTO.ChargerStatus chargerStatus);

    /**
     * 충전 속도와 충전기 상태에 따른 충전소 필터링
     * @param chargeSpeed 충전 속도
     * @param chargerStatus 충전기 상태
     */
    List<ChargingStationVO> getChargingStationsByFilter(ChargingStationDTO.ChargeSpeed chargeSpeed, ChargerDTO.ChargerStatus chargerStatus);

    /**
     * 사용자 위치 기반 충전소 조회
     * @param address 주소
     * @param chargeSpeed 충전 속도 필터
     * @param chargerStatus 충전기 상태 필터
     */
    List<ChargingStationVO> getChargingStationsByLocation(String address, String chargeSpeed, String chargerStatus);

    /**
     * 검색 기능 (지역명, 충전소명 검색)
     * @param query 검색어
     */
    List<ChargingStationVO> searchChargingStations(String query);

    /**
     * 공공 API로부터 충전소 데이터를 가져와 DB에 저장
     * @return 성공 여부
     */
    boolean fetchAndSaveChargingStations();

}
