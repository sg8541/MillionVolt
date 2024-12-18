package kr.co.milionvolt.ifive.controller.payment;

import kr.co.milionvolt.ifive.domain.payment.ChargeStationInfoDTO;
import kr.co.milionvolt.ifive.service.payment.ChargeStationInfoService;
import kr.co.milionvolt.ifive.service.payment.ChargeStationInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/payment")
public class ChargeStationInfoController {

    @Autowired
    ChargeStationInfoService chargeStationInfoService;

    @GetMapping("/printstationinfo/{stationId}")
    public ResponseEntity<ChargeStationInfoDTO> chargeStationInfo(@PathVariable int stationId) {

        ChargeStationInfoDTO chargeStationInfoDTO = chargeStationInfoService.printStatinInfo(stationId);
        ResponseEntity<ChargeStationInfoDTO> dto = ResponseEntity.ok(chargeStationInfoDTO);
        return dto;
    }
}
