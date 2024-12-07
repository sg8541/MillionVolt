package kr.co.milionvolt.ifive.controller.user;

import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.domain.charger.ChargerVO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationVO;
import kr.co.milionvolt.ifive.service.charger.ChargerService;
import kr.co.milionvolt.ifive.service.chargingstation.ChargingStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/main")
public class ChargingStationController {

    @Autowired
    private ChargingStationService chargingStationService;

    @Autowired
    private ChargerService chargerService;

    // 메인 페이지
    @GetMapping("/home")
    public String mainPage(Model model) {
        List<ChargingStationVO> nearbyStations = chargingStationService.getAllChargingStations(); // 주변 충전소 데이터
        model.addAttribute("stations", nearbyStations);
        return "main"; // main.html로 렌더링
    }

    // 충전소 검색
    @GetMapping("/search")
    @ResponseBody
    public List<ChargingStationVO> searchChargingStations(@RequestParam String query) {
        return chargingStationService.searchChargingStations(query);
    }

    // 특정 충전소 상세 조회
    @GetMapping("/{stationId}")
    @ResponseBody
    public ChargingStationVO getChargingStationDetails(@PathVariable Integer stationId) {
        List<ChargingStationVO> stationDetails = chargingStationService.getChargingStationById(stationId);
        return stationDetails.isEmpty() ? null : stationDetails.get(0); // 상세 정보 반환
    }

    // 충전소 필터링 (충전속도 기반)
    @GetMapping("/filter")
    @ResponseBody
    public List<ChargingStationVO> filterChargingStations(
            @RequestParam(required = false) String chargeSpeed,  // 예: "7kW"
            @RequestParam(required = false) String chargerStatus // 예: "사용 가능"
    ) {
        return chargingStationService.getChargingStationsByFilter(
                chargeSpeed != null ? ChargingStationDTO.ChargeSpeed.valueOf(chargeSpeed) : null,
                chargerStatus != null ? ChargerDTO.ChargerStatus.valueOf(chargerStatus) : null
        );
    }

    // 충전소와 충전기 데이터 DB 저장
    @PostMapping("/save")
    @ResponseBody
    public String saveChargingStationsFromApi() {
        // 공공 API에서 데이터를 받아와서 DB에 저장
        boolean result = chargingStationService.fetchAndSaveChargingStations();

        // 결과에 따라 메시지 반환
        if (result) {
            return "충전소 데이터를 성공적으로 저장했습니다.";
        } else {
            return "충전소 데이터 저장 중 오류가 발생했습니다.";
        }
    }

    // 충전소 상세 모달창 데이터
    @GetMapping("/{stationId}/modal")
    @ResponseBody
    public String getModalData(@PathVariable Integer stationId) {
        // 충전소 및 충전기 데이터 제공
        List<ChargingStationVO> stationDetails = chargingStationService.getChargingStationById(stationId);
        List<ChargerVO> chargers = chargerService.getChargersByStationId(stationId);

        // 데이터 JSON 변환 (임시 예시)
        return stationDetails.toString() + chargers.toString();
    }
}
