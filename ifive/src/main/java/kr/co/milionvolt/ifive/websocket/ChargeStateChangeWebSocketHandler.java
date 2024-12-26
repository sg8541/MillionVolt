package kr.co.milionvolt.ifive.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.domain.charger.ChargerVO;
import kr.co.milionvolt.ifive.service.charger.ChargerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ChargeStateChangeWebSocketHandler extends TextWebSocketHandler {
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    private final ChargerService chargerService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChargeStateChangeWebSocketHandler(ChargerService chargerService) {
        this.chargerService = chargerService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessions.put(session.getId(),session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println(payload);//가져올 값 charger90   00ae    `   `w
        int stationId = Integer.parseInt(payload);
        sendChagerStateUpdateData(session, stationId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("웹소켓 연결종료: " + session.getId());
    }

    private void sendChagerStateUpdateData(WebSocketSession session, int stationId) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(()-> {
            try {
                if(session == null || !session.isOpen()){
                    scheduler.shutdown();
                    return;
                }
             List<ChargerDTO> list =   chargerService.getChargersByStationId(stationId);
                if(!list.isEmpty()){
                    String jsonString = objectMapper.writeValueAsString(list);
                    System.out.println(jsonString);
                    session.sendMessage(new TextMessage(jsonString));
                }
                }catch (Exception e){
                       log.info("세션 종료시 데이터 못보내는 에러. ");
                }
            }, 0, 1, TimeUnit.SECONDS); // 1초마다 실행
        }
    }