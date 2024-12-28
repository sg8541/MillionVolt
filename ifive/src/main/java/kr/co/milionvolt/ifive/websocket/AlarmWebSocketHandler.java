package kr.co.milionvolt.ifive.websocket;

import kr.co.milionvolt.ifive.domain.penaltie.PenaltieDTO;
import kr.co.milionvolt.ifive.domain.penaltie.PenaltiechargerStatusCheckVO;
import kr.co.milionvolt.ifive.domain.penaltie.PenaltyCheckVO;
import kr.co.milionvolt.ifive.entity.ReservationRedis;
import kr.co.milionvolt.ifive.service.penalty.PenaltyService;
import kr.co.milionvolt.ifive.service.reservation.ReservationRedisService;
import kr.co.milionvolt.ifive.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class AlarmWebSocketHandler extends TextWebSocketHandler {
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> uid = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> repeatNum = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> amount = new ConcurrentHashMap<>();


    private final UserService userService;
    private final ReservationRedisService reservationRedisService;
    private final PenaltyService penaltyService;
    private int num = 0;
    private int penaltyAmount = 0;



    public AlarmWebSocketHandler(UserService userService, ReservationRedisService reservationRedisService, PenaltyService penaltyService) {
        this.userService = userService;
        this.reservationRedisService = reservationRedisService;
        this.penaltyService = penaltyService;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        log.info("웹소켓 연결 확인.");

    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        int userId = userService.selectUserId(payload); // 클라이언트가 보낸 ID로 조회
        uid.put(payload, userId); // 사용자 ID와 이름 매핑
        sessions.put(payload, session); // 세션 저장

        // 1분마다 예약 시간 확인 및 알림
        scheduleAlarms(session, userId);

    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("웹소켓 연결종료 : "+ session.getId());
        sessions.remove(uid);
        sessions.remove(repeatNum);
    }


    private void scheduleAlarms(WebSocketSession session , int userId) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                    if (session == null || !session.isOpen()) {
                        log.info("Session closed for userId: " + userId);
                        scheduler.shutdown();
                        return;
                    }
                LocalDateTime now = LocalDateTime.now();
                List<ReservationRedis> reservations = reservationRedisService.findReservationInfoByUserId(userId);
                if (reservations == null || reservations.isEmpty()) {
                    log.info("No reservations found for userId: " + userId);
                }
                for (ReservationRedis reservation : reservations) {
                    LocalDateTime reservationTime = reservation.getStartTime();
                    LocalDateTime reservationEndTime  = reservation.getEndTime();
                    if (reservationTime.isBefore(now.plusMinutes(1)) && reservationTime.isAfter(now.minusMinutes(1))) {
                        String status = String.format(
                                "{\"reservationId\": \"%d\", \"startTime\": \"%s\", \"message\": \"%s\" ,\"stationId\": \"%d\"}",
                                reservation.getReservationId(),
                                reservationTime,
                                "충전을 시작해주세요!",
                                reservation.getStationId()
                        );
                        session.sendMessage(new TextMessage(status));
                    }
                    if(reservationEndTime.isAfter(now.minusSeconds(30))&& reservationEndTime.isBefore(now.plusSeconds(30))){
                        PenaltiechargerStatusCheckVO penaltiechargerStatusCheckVO;
                        penaltiechargerStatusCheckVO =  penaltyService.findChargerId(reservation.getReservationId(),reservation.getStationId(),reservation.getChargerId());

                        // 해당 충전기 번호 찾기
                        if(penaltiechargerStatusCheckVO.getChargerStatusId()==2){
                            LocalDateTime closeReservationTime= penaltyService.findCloseStratTime(reservationEndTime,penaltiechargerStatusCheckVO.getChargerId(), penaltiechargerStatusCheckVO.getStationId());
                            int resNum =  reservation.getReservationId();
                            if(closeReservationTime != null) {
                                penaltiySendAlarm(session, closeReservationTime, resNum, reservationEndTime);
                            }
                        }
                    }
                 }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 0, 1, TimeUnit.MINUTES); // 1분마다 실행
         }

    private void penaltiySendAlarm(WebSocketSession session, LocalDateTime closeReservationTime, int resNum, LocalDateTime reservationEndTime){
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

            scheduler.scheduleAtFixedRate(()-> {
                try{
                    if (session == null || !session.isOpen()) {
                        log.info("세션종료");
                        scheduler.shutdown();
                        return;
                    }
                    LocalDateTime now = LocalDateTime.now();
                    if(now.isBefore(closeReservationTime.minusMinutes(15))) {
                        String status = String.format("{\"closeReservationTime\": \"%s\", \"message\": \"%s\"}", closeReservationTime, "출차해주세요!");
                        session.sendMessage(new TextMessage(status));

                    } else if (now.isAfter(closeReservationTime.minusMinutes(15)) && now.isBefore(closeReservationTime)) { // 예약시간으로부터 15분전 알림.
                        String status = String.format("{\"closeReservationTime\": \"%s\", \"message\": \"%s\"}", closeReservationTime, "뒷 예약시간 15분 전 입니다.");
                        PenaltyCheckVO vo = penaltyService.penaltyCheckVo(resNum);
                        if(vo == null) {
                            penaltyService.insertPenaltyRefund(Timestamp.valueOf(now), resNum, "possible");
                        }
                        session.sendMessage(new TextMessage(status));
                    } else {
                        PenaltyCheckVO vo = penaltyService.penaltyCheckVo(resNum);
                            if (repeatNum.get(String.valueOf(resNum)) == null) {
                                repeatNum.put(String.valueOf(resNum), 1);
                                penaltyAmount = 100 + vo.getPenaltyAmount();
                                amount.put(String.valueOf(resNum), penaltyAmount);
                            } else {
                                int currentRepeat = repeatNum.get(String.valueOf(resNum));
                                currentRepeat++;
                                repeatNum.put(String.valueOf(resNum), currentRepeat);
                                penaltyAmount += 100;
                                amount.put(String.valueOf(resNum), penaltyAmount);
                            }
                            penaltyService.updatePenalty(penaltyAmount, resNum);

                        String status = String.format("{\"penaltyAmount\": \"%d\", \"message\": \"%s\"}", penaltyAmount,"벌금 부여");
                        session.sendMessage(new TextMessage(status));

                    }
                }catch (Exception e){
                   e.printStackTrace();
                }
            },0,1,TimeUnit.MINUTES); // 1분마다 실행.
        }
    }