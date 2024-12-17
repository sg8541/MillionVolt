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

    // 3. 충전소 기본 정보 반환
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

    // 4. 충전소와 연결된 충전기 상태 정보 반환
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



    // 1. 통합된 충전소 검색 및 필터링 API (검색 + 필터링 + 페이징 처리)
    @GetMapping("/sidebar")
    public ResponseEntity<List<ChargingStationVO>> getFilteredStations(
            @RequestParam(required = false) String query, // 검색어 (옵션)
            @RequestParam(required = false) Integer chargerSpeedId, // 충전 속도 필터링 (옵션)
            @RequestParam(defaultValue = "1") int page, // 현재 페이지 (기본값: 1)
            @RequestParam(defaultValue = "5") int size // 페이지당 데이터 수 (기본값: 5)
    ) {
        // 결과 데이터 초기화
        List<ChargingStationVO> stations;

        // 검색 및 필터링 처리
        if (query != null && !query.isBlank()) {
            // 검색어가 주어진 경우: 검색어 기반 충전소 조회
            System.out.println("검색어로 충전소 조회: query=" + query + ", page=" + page + ", size=" + size);
            stations = chargingStationService.searchChargingStations(query, page, size);
        } else if (chargerSpeedId != null) {
            // 충전 속도로 필터링
            System.out.println("충전 속도로 필터링: chargerSpeedId=" + chargerSpeedId);
            stations = chargingStationService.filterByChargeSpeed(chargerSpeedId, null);
        } else {
            // 전체 충전소 조회
            System.out.println("전체 충전소 조회 (페이징 포함): page=" + page + ", size=" + size);
            stations = chargingStationService.getAllChargingStations(page, size);
        }

        // 결과 로그
        if (stations.isEmpty()) {
            System.out.println("조건에 맞는 충전소 데이터가 없습니다.");
        } else {
            System.out.println("조회된 충전소 데이터 개수: " + stations.size());
        }

        return ResponseEntity.ok(stations);
    }

    // 2. 기존 검색 엔드포인트 수정 (뷰와 연동용 - HTML 반환)
    @GetMapping("/search")
    public String searchStations(
            @RequestParam String query, // 검색어
            @RequestParam(defaultValue = "1") int page, // 현재 페이지
            @RequestParam(defaultValue = "5") int size, // 페이지당 데이터 수
            Model model
    ) {
        // 검색 결과 가져오기
        List<ChargingStationVO> searchResults = chargingStationService.searchChargingStations(query, page, size);

        // 모델에 데이터 추가
        model.addAttribute("chargingStations", searchResults);
        model.addAttribute("query", query);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) searchResults.size() / size));

        return "main"; // 렌더링할 뷰 이름
    }

    // 3. 기존 페이징 처리된 검색 결과 API
    @GetMapping("/search-paginated")
    @ResponseBody
    public ResponseEntity<List<ChargingStationVO>> getPaginatedSearchResults(
            @RequestParam String query, // 검색어
            @RequestParam(defaultValue = "1") int page, // 현재 페이지
            @RequestParam(defaultValue = "5") int size // 페이지당 데이터 수
    ) {
        List<ChargingStationVO> paginatedResults = chargingStationService.searchChargingStations(query, page, size);
        return ResponseEntity.ok(paginatedResults);
    }

    // 9. 카카오 지도 상 마커 표시 데이터
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

    // 충전기 상태 변경 API
    @PutMapping("/chargers/{chargerId}/status")
    public ResponseEntity<?> updateChargerStatus(
            @PathVariable Integer chargerId,
            @RequestBody Map<String, String> payload) {
        try {
            String statusStr = payload.get("status"); // "available", "in_use", "maintenance"
            Integer stationId = payload.get("stationId") != null ? Integer.parseInt(payload.get("stationId")) : null;

            if (stationId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("stationId는 필수입니다.");
            }

            // 문자열 상태를 ID 값으로 변환
            Integer statusId = switch (statusStr.toLowerCase()) {
                case "available" -> 1;
                case "in_use" -> 2;
                case "maintenance" -> 3;
                default -> throw new IllegalArgumentException("Invalid status value: " + statusStr);
            };

            // 서비스 호출
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
