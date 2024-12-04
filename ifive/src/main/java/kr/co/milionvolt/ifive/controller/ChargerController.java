package kr.co.milionvolt.ifive.controller;

import kr.co.milionvolt.ifive.domain.ChargerDTO;
import kr.co.milionvolt.ifive.domain.ChargerVO;
import kr.co.milionvolt.ifive.service.ChargerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/charger")
public class ChargerController {

    @Autowired
    private ChargerService chargerService;

    public ChargerController(ChargerService chargerService) {
        this.chargerService = chargerService;
    }

    // 특정 충전소의 충전기 목록 조회
    @GetMapping("/list/{stationId}")
    public ResponseEntity<List<ChargerVO>> getChargersByStationId(@PathVariable Integer stationId) {
        List<ChargerVO> chargers = chargerService.getChargersByStationId(stationId);
        return ResponseEntity.ok(chargers);
    }

    // 특정 충전소의 특정 충전기 상태 조회
    @GetMapping("/{stationId}/charger/{chargerId}/status")
    public ResponseEntity<ChargerDTO.ChargerStatus> getChargerStatus(@PathVariable Integer stationId,
                                                                     @PathVariable Integer chargerId) {
        ChargerDTO.ChargerStatus chargerStatus = chargerService.getChargerStatusByStationIdAndChargerId(stationId, chargerId);
        return ResponseEntity.ok(chargerStatus);
    }

    // 충전기 상태 변경
    @PutMapping("/status/{chargerId}")
    public ResponseEntity<String> updateChargerStatus(@PathVariable Integer chargerId, @RequestParam String status) {
        boolean isUpdated = chargerService.updateChargerStatus(chargerId, status);
        return isUpdated ? ResponseEntity.ok("충전기 상태가 변경되었습니다.")
                : ResponseEntity.status(400).body("충전기 상태 변경 실패");
    }

}
