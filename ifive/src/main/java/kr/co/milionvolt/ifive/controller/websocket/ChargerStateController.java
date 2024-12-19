package kr.co.milionvolt.ifive.controller.websocket;

import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.domain.charger.ChargerWebSocketStompDTO;
import kr.co.milionvolt.ifive.service.charger.ChargerService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
public class ChargerStateController {

    private final ChargerService chargerService;
    private final SimpMessagingTemplate messagingTemplate;


    public ChargerStateController(ChargerService chargerService, SimpMessagingTemplate messagingTemplate) {
        this.chargerService = chargerService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/charger")
    @SendTo("/topic/charger")
    public List<ChargerDTO> getChargerState(ChargerWebSocketStompDTO dto) {
        int stationId = dto.getStationId();
        System.out.println("번호 "+ stationId);
        List<ChargerDTO> state = chargerService.getChargersByStationId(stationId);
        if(state !=null){
            broadcastChargerState(stationId);
        }
        return state;
    }

    // 주기적으로 모든 클라이언트에 상태 업데이트 전송
    public void broadcastChargerState(int stationId) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(()-> {
             // 또는 다른 방식으로 가져옴
            List<ChargerDTO> list = chargerService.getChargersByStationId(stationId); //일단 직접 입력.
            System.out.println("STOMP :모든 리스트 : :"+list);
            // 브로커를 통해 모든 구독자에게 데이터 전송
            if (!list.isEmpty()) {
                messagingTemplate.convertAndSend("/topic/chargerstate", list);
            }
        },0,1, TimeUnit.SECONDS);
    }

}
