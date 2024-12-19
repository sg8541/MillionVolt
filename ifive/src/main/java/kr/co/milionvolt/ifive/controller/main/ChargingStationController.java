package kr.co.milionvolt.ifive.controller.main;

import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationVO;
import kr.co.milionvolt.ifive.service.charger.ChargerServiceImpl;
import kr.co.milionvolt.ifive.service.chargingstation.ChargingStationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/charging-stations")
public class ChargingStationController {

    private final ChargingStationService chargingStationService;
    private final ChargerServiceImpl chargerServiceImpl;

    public ChargingStationController(ChargingStationService chargingStationService, ChargerServiceImpl chargerServiceImpl) {
        this.chargingStationService = chargingStationService;
        this.chargerServiceImpl = chargerServiceImpl;
    }

    // 1. 메인 페이지: 사용자 주변 충전소 목록 조회 및 렌더링
    @GetMapping("/main")
    public String getNearbyStations(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Integer chargerSpeedId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        if (address == null || address.isEmpty()) {
            address = "서울"; // 기본값 설정
        }
        List<ChargingStationVO> stations = chargingStationService.getNearbyStations(address, chargerSpeedId);
        // 페이징 처리
        int start = (page - 1) * size;
        int end = Math.min(start + size, stations.size());
        // 첫 번째 충전소를 selectedStation으로 설정 (예제)
        ChargingStationVO selectedStation = stations.isEmpty() ? null : stations.get(0);
        model.addAttribute("chargingStations", stations);
        model.addAttribute("selectedStation", selectedStation); // selectedStation 전달
        model.addAttribute("address", address);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) stations.size() / size));
        return "main"; // main.html
    }

    // 2. 충전소 기본 정보 반환
    @GetMapping("/station/{stationId}")
    public ResponseEntity<ChargingStationDTO> getStationDetails(@PathVariable Integer stationId) {
        try {
            // 충전소 데이터 조회
            ChargingStationDTO stationDetails = chargingStationService.getStationWithChargers(stationId);

            // 데이터가 없을 경우 처리
            if (stationDetails == null) {
                System.err.println("Station not found with ID: " + stationId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            return ResponseEntity.ok(stationDetails);
        } catch (Exception e) {
            // 예외 처리
            System.err.println("Error fetching station details for ID: " + stationId);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 3. 충전소와 연결된 충전기 상태 정보 반환
    @GetMapping("/station/{stationId}/chargers")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getChargersByStationId(@PathVariable Integer stationId) {
        try {
            // 조인 쿼리를 통해 충전기 상세 데이터 조회
            List<Map<String, Object>> chargers = chargerServiceImpl.getChargersWithDetails(stationId);

            // 충전기 데이터가 없을 경우 처리
            if (chargers == null || chargers.isEmpty()) {
                System.err.println("No chargers found for station ID: " + stationId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
            }

            return ResponseEntity.ok(chargers);
        } catch (Exception e) {
            // 예외 처리
            System.err.println("Error fetching chargers for station ID: " + stationId);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    // 4. 통합된 충전소 검색 및 필터링 API (검색 + 필터링 + 페이징 처리)
    @GetMapping("/sidebar")
    public ResponseEntity<Map<String, Object>> getFilteredStations(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Integer chargerSpeedId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        List<ChargingStationVO> stations;
        int totalCount;

        try {
            // 충전소 필터링 로직
            if (query != null && !query.isBlank()) {
                stations = chargingStationService.searchChargingStations(query, page, size);
                totalCount = chargingStationService.countSearchResults(query);
            } else if (chargerSpeedId != null) {
                stations = chargingStationService.filterByChargeSpeed(chargerSpeedId, page, size);
                totalCount = chargingStationService.countStationsByChargeSpeed(chargerSpeedId);
            } else {
                stations = chargingStationService.getAllChargingStations(page, size);
                totalCount = chargingStationService.countAllChargingStations();
            }

            // 충전소별 충전기 정보 추가
            List<Map<String, Object>> stationsWithChargers = stations.stream()
                    .map(station -> {
                        Map<String, Object> stationWithChargers = new HashMap<>();
                        stationWithChargers.put("station", station);

                        // 충전소 ID를 기준으로 충전기 정보 가져오기
                        List<ChargerDTO> chargers = chargerServiceImpl.getChargersByStationId(station.getStationId());
                        stationWithChargers.put("chargers", chargers);

                        // 사용 가능한 충전기 개수 계산
                        long availableChargers = chargers.stream()
                                .filter(charger -> charger.getChargerStatusId() == 1)
                                .count();
                        stationWithChargers.put("availableChargerCount", availableChargers);

                        // 충전기 속도 데이터 추가
                        stationWithChargers.put("chargeSpeedIds", chargers.stream()
                                .map(ChargerDTO::getChargerSpeedId)
                                .distinct()
                                .toList()); // 중복 제거하여 속도 ID 리스트로 추가

                        return stationWithChargers;
                    }).toList();

            // 응답 데이터 구성
            Map<String, Object> response = new HashMap<>();
            response.put("stations", stationsWithChargers);
            response.put("totalCount", totalCount);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // 예외 처리
            System.err.println("Error fetching stations or chargers: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyMap());
        }
    }

    // 5. 카카오 지도 상 마커 표시 데이터
    @GetMapping("/markers")
    public ResponseEntity<List<ChargingStationVO>> getMarkersByAddress(
            @RequestParam(required = false) String address
    ) {
        System.out.println("API 요청: address=" + address);
        List<ChargingStationVO> markers = chargingStationService.getStationsByAddress(address);

        if (markers.isEmpty()) {
            System.out.println("검색된 충전소 데이터가 없습니다: address=" + address);
        } else {
            System.out.println("검색된 충전소 데이터: " + markers);
        }
        return ResponseEntity.ok(markers);
    }

    // 6. 충전기 상태 변경 API
    @PutMapping("/chargers/{chargerId}/status")
    public ResponseEntity<?> updateChargerStatus(
            @PathVariable Integer chargerId,
            @RequestBody Map<String, Object> payload) {
        try {
            // 1. Payload에서 값 추출
            Integer stationId = payload.get("stationId") != null ? Integer.parseInt(payload.get("stationId").toString()) : null;
            Object statusObj = payload.get("status");

            // 2. stationId 검증
            if (stationId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("stationId는 필수입니다.");
            }

            // 3. 상태 값 처리
            Integer statusId;
            if (statusObj instanceof String) {
                statusId = switch (statusObj.toString().toLowerCase()) {
                    case "available" -> 1;
                    case "in_use" -> 2;
                    case "maintenance" -> 3;
                    default -> throw new IllegalArgumentException("Invalid status value: " + statusObj);
                };
            } else if (statusObj instanceof Integer) {
                statusId = (Integer) statusObj;
                if (statusId < 1 || statusId > 3) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid numeric status value: " + statusId);
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status value format.");
            }

            // 4. 서비스 호출
            boolean isUpdated = chargerServiceImpl.updateChargerStatus(stationId, chargerId, statusId);
            if (isUpdated) {
                return ResponseEntity.ok("충전기 상태가 성공적으로 업데이트되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("충전기를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("충전기 상태 변경 중 오류가 발생했습니다.");
        }
    }



}
