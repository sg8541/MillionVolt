package kr.co.milionvolt.ifive.controller.main;

import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationVO;
import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.service.chargingstation.ChargingStationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/stations")
public class ChargingStationController {

    private final ChargingStationService chargingStationService;

    public ChargingStationController(ChargingStationService chargingStationService) {
        this.chargingStationService = chargingStationService;
    }

    // 1. 사용자 위치 기반 충전소 목록 조회 및 필터링
    @GetMapping
    public ResponseEntity<List<ChargingStationVO>> getStations(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) ChargingStationDTO.ChargeSpeed chargeSpeed,
            @RequestParam(required = false) ChargerDTO.ChargerStatus chargerStatus) {
        List<ChargingStationVO> stations = chargingStationService.getChargingStationsByLocation(address, chargeSpeed, chargerStatus);
        return ResponseEntity.ok(stations);
    }

    // 2. 특정 충전소 상세 정보 조회
    @GetMapping("/{stationId}")
    public ResponseEntity<ChargingStationVO> getStationDetails(@PathVariable Integer stationId) {
        List<ChargingStationVO> station = chargingStationService.getChargingStationById(stationId);
        if (station.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(station.get(0));
    }

    // 3. 충전소 검색
    @GetMapping("/search")
    public ResponseEntity<List<ChargingStationDTO>> searchStations(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        // 검색 결과 가져오기
        List<ChargingStationVO> allStations = chargingStationService.searchChargingStations(query);

        // 페이징 처리
        int start = (page - 1) * size;
        int end = Math.min(start + size, allStations.size());
        List<ChargingStationDTO> paginatedStations = allStations.subList(start, end).stream()
                .map(station ->

                        new ChargingStationDTO(
                        station.getStationId(),
                        station.getName(),
                        station.getAvailableCharger(),
                        station.getChargeSpeed(),
                        station.getPricePerKWh()))
                .toList();

        return ResponseEntity.ok(paginatedStations);
    }

    // 4. 페이징 처리된 충전소 목록 조회
    @GetMapping("/paginated")
    public ResponseEntity<List<ChargingStationVO>> getPaginatedStations(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        List<ChargingStationVO> paginatedStations = chargingStationService.getAllChargingStations()
                .stream()
                .skip((long) (page - 1) * size)
                .limit(size)
                .toList();
        return ResponseEntity.ok(paginatedStations);
    }

    // 5. 모달창 상세 정보 제공 (사업자 정보, 전화번호 포함)
    @GetMapping("/{stationId}/details")
    public ResponseEntity<ChargingStationVO> getModalDetails(@PathVariable Integer stationId) {
        ChargingStationVO stationDetails = chargingStationService.getChargingStationById(stationId).stream().findFirst().orElse(null);
        if (stationDetails == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stationDetails);
    }

}
