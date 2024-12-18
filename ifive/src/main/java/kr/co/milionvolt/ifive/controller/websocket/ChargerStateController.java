package kr.co.milionvolt.ifive.controller.websocket;

import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.service.charger.ChargerService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ChargerStateController {

    private final ChargerService chargerService;
    private final SimpMessagingTemplate messagingTemplate;


    public ChargerStateController(ChargerService chargerService, SimpMessagingTemplate messagingTemplate) {
        this.chargerService = chargerService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/charger")
    public List<ChargerDTO> getChargerState(int stationId) {
        List<ChargerDTO> state = chargerService.getChargersByStationId(stationId);
        return state;
    }

    // 주기적으로 모든 클라이언트에 상태 업데이트 전송
    @Scheduled(fixedRate = 1000) // 1초마다 실행
    public void broadcastChargerState() {
        int stationId = 1; // 또는 다른 방식으로 가져옴
        List<ChargerDTO> list = chargerService.getChargersByStationId(stationId);
        System.out.println("STOMP :모든 리스트 : "+list);
        // 브로커를 통해 모든 구독자에게 데이터 전송
        if (!list.isEmpty()) {
            messagingTemplate.convertAndSend("/topic/chargerstate", list);
        }
    }

}
