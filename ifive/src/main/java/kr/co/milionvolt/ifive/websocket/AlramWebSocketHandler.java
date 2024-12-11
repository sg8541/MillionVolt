package kr.co.milionvolt.ifive.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AlramWebSocketHandler extends TextWebSocketHandler {
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put("alarm",session);

    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Reservation WebSocket Message: " + message.getPayload());
        String payload = message.getPayload();
        // redis 예약 객체 만들어서

        //service단 의존성 주입 후 조회.


    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Reservation WebSocket Connection Closed: " + session.getId());
    }

    private boolean isReservationTime(){

        return true;
    }

    private void sendAlert(WebSocketSession session) throws IOException {


    }
}
