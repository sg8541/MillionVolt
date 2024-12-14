package kr.co.milionvolt.ifive.websocket;

import kr.co.milionvolt.ifive.entity.ReservationRedis;
import kr.co.milionvolt.ifive.service.reservation.ReservationRedisService;
import kr.co.milionvolt.ifive.service.user.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class AlarmWebSocketHandler extends TextWebSocketHandler {
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> uid = new ConcurrentHashMap<>();


    private final UserService userService;
    private final ReservationRedisService reservationRedisService;


    public AlarmWebSocketHandler(UserService userService, ReservationRedisService reservationRedisService) {
        this.userService = userService;
        this.reservationRedisService = reservationRedisService;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        System.out.println("웹소켓 연결 확인.");

    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println(payload);
        int userId = userService.selectUserId(payload); // 클라이언트가 보낸 ID로 조회
        System.out.println("User ID: " + userId);

        uid.put(payload, userId); // 사용자 ID와 이름 매핑
        sessions.put(payload, session); // 세션 저장


      //  System.out.println("Reservations for userId " + userId + ": " + reservations);

        // 1분마다 예약 시간 확인 및 알림
        scheduleAlarms(session, userId);

    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("웹소켓 연결종료: " + session.getId());


    }


private void scheduleAlarms(WebSocketSession session , int userId) {
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    scheduler.scheduleAtFixedRate(() -> {
        try {
                if (session == null || !session.isOpen()) {
                    System.out.println("Session closed for userId: " + userId);
                    scheduler.shutdown();
                    return;
                }
            LocalDateTime now = LocalDateTime.now();
            System.out.println("현재시간 : "+now);
            List<ReservationRedis> reservations = reservationRedisService.findReservationInfoByUserId(userId);
            for (ReservationRedis reservation : reservations) {
                LocalDateTime reservationTime = reservation.getStartTime();
                System.out.println("예약한 시간 : "+reservationTime);
                if (reservationTime.isBefore(now.plusMinutes(1)) && reservationTime.isAfter(now.minusMinutes(1))) {
                    String status = String.format(
                            "{\"reservationId\": \"%d\", \"startTime\": \"%s\", \"message\": \"%s\"}",
                            reservation.getReservationId(),
                            reservationTime,
                            "예약시간입니다! 충전을 시작해주세요."
                    );
                    System.out.println(reservation.getReservationId());

                    session.sendMessage(new TextMessage(status));
                    }
             }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.MINUTES); // 1분마다 실행
    }
}