package kr.co.milionvolt.ifive.controller;

import kr.co.milionvolt.ifive.domain.notification.ChargingStatusDTO;
import kr.co.milionvolt.ifive.service.charging.ChargingStatusSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ChargingStatusController {
    @Autowired
    private ChargingStatusSerivce chargingStatusSerivce;

    @GetMapping("/charging/{userId}/{reservationId}")
    public ResponseEntity<ChargingStatusDTO> chargingStaus(@PathVariable String userId,
                                                           @PathVariable int reservationId ){
        ChargingStatusDTO dto =  chargingStatusSerivce.chargingStatus(userId,reservationId);
        ResponseEntity<ChargingStatusDTO> entity = new ResponseEntity<>(dto, HttpStatus.OK);
        return entity;
    }
}
