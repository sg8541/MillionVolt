package kr.co.milionvolt.ifive.controller.main;

import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationVO;
import kr.co.milionvolt.ifive.service.charger.ChargerServiceImpl;
import kr.co.milionvolt.ifive.service.chargingstation.ChargingStationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
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

    // 사용자 현재 위치 기반 충전소 가져오기
    @GetMapping("/nearby")
    @ResponseBody
    public ResponseEntity<List<ChargingStationVO>> getNearbyStationsByLocation(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "5") int radius // 반경 설정 (단위: km)
    ) {
        List<ChargingStationVO> nearbyStations = chargingStationService.getStationsByLocation(latitude, longitude, radius);
        return ResponseEntity.ok(nearbyStations);
    }

    // 충전소 기본 정보 반환
    @GetMapping("/station/{stationId}")
    public ResponseEntity<ChargingStationDTO> getStationDetails(@PathVariable Integer stationId) {
        ChargingStationDTO stationDetails = chargingStationService.getStationWithChargers(stationId);
        return ResponseEntity.ok(stationDetails);
    }

    // 충전소와 연결된 충전기 상태 정보 반환
    @GetMapping("/station/{stationId}/chargers")
    @ResponseBody
    public ResponseEntity<List<ChargerDTO>> getChargersByStationId(@PathVariable Integer stationId) {
        List<ChargerDTO> chargers = chargerServiceImpl.getChargersByStationId(stationId); // 충전기 데이터 가져오기
        if (chargers == null || chargers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(chargers);
    }

    // 3. 충전소 검색
    @GetMapping("/search")
    public String searchStations(
            @RequestParam String query, Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        List<ChargingStationVO> searchResults = chargingStationService.searchChargingStations(query, page, size);
        model.addAttribute("chargingStations", searchResults);
        model.addAttribute("query", query);
        model.addAttribute("currentPage", page);
        return "main";
    }

    // 6. 페이징 처리된 충전소 검색 결과
    @GetMapping("/search-paginated")
    @ResponseBody
    public ResponseEntity<List<ChargingStationVO>> getPaginatedSearchResults(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        List<ChargingStationVO> paginatedResults = chargingStationService.searchChargingStations(query, page, size);
        return ResponseEntity.ok(paginatedResults);
    }

    // 4. 충전소 필터링 (충전속도 선택)
    @GetMapping("/filter")
    @ResponseBody
    public ResponseEntity<List<ChargingStationVO>> filterStations(
            @RequestParam Integer chargerSpeedId,
            @RequestParam(required = false) String address) {
        if (address == null || address.isEmpty()) {
            address = "Seoul"; // 기본 주소
        }
        List<ChargingStationVO> filteredStations = chargingStationService.filterByChargeSpeed(chargerSpeedId, address);
        return ResponseEntity.ok(filteredStations);
    }

    // 5. 사용자 주변 충전소와 상태 간단히 보기 (사이드바)
    @GetMapping("/sidebar")
    public ResponseEntity<List<ChargingStationVO>> getSidebarStations(
            @RequestParam(required = false) String address) {
        if (address == null || address.isEmpty()) {
            address = "서울"; // 기본 주소
        }
        List<ChargingStationVO> sidebarStations = chargingStationService.getSidebarStations(address);
        return ResponseEntity.ok(sidebarStations);
    }

    // 7. 카카오 지도 상 마커 표시 데이터
    @GetMapping("/markers")
    public ResponseEntity<List<ChargingStationVO>> getMarkers(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Integer chargerSpeedId) {
        if (address == null || address.isEmpty()) {
            address = "서울";
        }

        // 디버깅 로그 추가
        System.out.println("Address: " + address + ", ChargerSpeedId: " + chargerSpeedId);

        List<ChargingStationVO> markers = chargingStationService.getNearbyStations(address, chargerSpeedId);

        // 반환 데이터 확인
        markers.forEach(marker -> System.out.println("Marker: " + marker));

        return ResponseEntity.ok(markers);
    }

}
