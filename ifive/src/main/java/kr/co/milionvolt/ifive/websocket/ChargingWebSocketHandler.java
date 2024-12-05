package kr.co.milionvolt.ifive.websocket;

import kr.co.milionvolt.ifive.service.charging.ChargingStatusSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

public class ChargingWebSocketHandler extends TextWebSocketHandler {
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Autowired
    private ChargingStatusSerivce chargingStatusSerivce;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(),session);
        System.out.println("웹소켓 연결됨."+session.getId());
        sendChargingStatus(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        String payload = message.getPayload();
        System.out.println("메세지 수신 : "+payload);

        if ("start".equals(payload)) {
            startCharging(session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        System.out.println("웹소켓 연결 종료" + session.getId());
        sessions.remove(session.getId());

    }

    private void sendChargingStatus(WebSocketSession session) {


    }
    private void startCharging(WebSocketSession session) {


    }

}
