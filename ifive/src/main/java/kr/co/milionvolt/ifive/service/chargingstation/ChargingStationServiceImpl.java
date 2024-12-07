package kr.co.milionvolt.ifive.service.chargingstation;

import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationVO;
import kr.co.milionvolt.ifive.mapper.ChargingStationMapper;
import kr.co.milionvolt.ifive.util.PublicApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChargingStationServiceImpl implements ChargingStationService {

    @Autowired
    private ChargingStationMapper chargingStationMapper;

    @Autowired
    private PublicApiClient publicApiClient;

    /**
     * 전체 충전소 조회(충전기 타입과 충전기 상태 포함)
     */
    @Override
    public List<ChargingStationVO> getAllChargingStations() {
        return chargingStationMapper.getAllChargingStationList();
    }

    /**
     * 충전소 개별 조회
     */
    @Override
    public List<ChargingStationVO> getChargingStationById(Integer stationId) {
        return chargingStationMapper.getOneChargingStationList(stationId);
    }

    /**
     * 충전소 수정
     */
    @Override
    public boolean updateChargingStation(ChargingStationDTO chargingStationDTO) {
        int updatedRows = chargingStationMapper.updateChargingStation(chargingStationDTO);
        return updatedRows > 0;
    }

    /**
     * 충전기 정보 수정 (충전기 상태, 충전기 타입)
     */
    @Override
    public boolean updateChargerInfo(Integer chargerId, ChargerDTO.ChagerType chagerType, ChargerDTO.ChargerStatus chargerStatus) {
        int updatedRows = chargingStationMapper.updateChargerInfo(chargerId, chagerType, chargerStatus);
        return updatedRows > 0;
    }

    /**
     * 충전소의 충전 속도와 충전기 상태에 따른 필터링
     */
    @Override
    public List<ChargingStationVO> getChargingStationsByFilter(ChargingStationDTO.ChargeSpeed chargeSpeed, ChargerDTO.ChargerStatus chargerStatus) {
        return chargingStationMapper.getChargingStationsByFilter(chargeSpeed, chargerStatus);
    }

    /**
     * 사용자 위치 기반 충전소 목록 반환 (주소 기반)
     */
    @Override
    public List<ChargingStationVO> getChargingStationsByLocation(String address,
                                                                 String chargeSpeed, String chargerStatus) {
        // 주소를 기반으로 DB 검색 로직 호출
        return chargingStationMapper.getChargingStationsByLocation(address, chargeSpeed, chargerStatus);
    }

    /**
     * 검색
     */
    @Override
    public List<ChargingStationVO> searchChargingStations(String query) {
        return chargingStationMapper.searchChargingStations(query);
    }

    /**
     * 공공데이터 API 호출 후 충전소 정보 저장
     */
    public boolean fetchAndSaveChargingStations() {
        try {
            List<ChargingStationDTO> apiResponse = publicApiClient.fetchChargingStationData();
            for (ChargingStationDTO dto : apiResponse) {
                // 데이터가 기존에 있는지 확인 후 삽입 또는 업데이트
                ChargingStationVO existingStation = chargingStationMapper.getChargingStationByAddress(dto.getAddress());
                if (existingStation == null) {
                    chargingStationMapper.insertChargingStation(dto);
                } else {
                    chargingStationMapper.updateChargingStation(dto);
                }
            }
            return true;
        } catch (Exception e) {
            // 예외 처리 및 로깅
            System.err.println("Error fetching or saving charging station data: " + e.getMessage());
            return false;
        }
    }

}
