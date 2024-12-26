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
                System.out.println("알람 1분 동작.");
                LocalDateTime now = LocalDateTime.now();
                System.out.println("현재 시간 : "+now);
                List<ReservationRedis> reservations = reservationRedisService.findReservationInfoByUserId(userId);
                if (reservations == null || reservations.isEmpty()) {
                    System.out.println("No reservations found for userId: " + userId);
                }
                for (ReservationRedis reservation : reservations) {
                    LocalDateTime reservationTime = reservation.getStartTime();
                    LocalDateTime reservationEndTime  = reservation.getEndTime();
                    System.out.println("예약 종료 내역 확인 : "+ reservationEndTime);
                    if (reservationTime.isBefore(now.plusMinutes(1)) && reservationTime.isAfter(now.minusMinutes(1))) {
                        String status = String.format(
                                "{\"reservationId\": \"%d\", \"startTime\": \"%s\", \"message\": \"%s\" ,\"stationId\": \"%d\"}",
                                reservation.getReservationId(),
                                reservationTime,
                                "예약시간) 충전을 시작해주세요.",
                                reservation.getStationId()
                        );
                        System.out.println("예약 발송 : "+reservation);
                        session.sendMessage(new TextMessage(status));
                    }
                    if(reservationEndTime.isBefore(now.plusMinutes(1))&& reservationEndTime.isAfter(now.minusMinutes(1))){
                        System.out.println("예약 종료한 시간 : "+reservationEndTime);
                        PenaltiechargerStatusCheckVO penaltiechargerStatusCheckVO;
                        penaltiechargerStatusCheckVO =  penaltyService.findChargerId(reservation.getReservationId(),reservation.getStationId(),reservation.getChargerId());
                        System.out.println("확인 :  "+penaltiechargerStatusCheckVO);
                        // 해당 충전기 번호 찾기
                        if(penaltiechargerStatusCheckVO.getChargerStatusId()==2){
                            System.out.println("예약 종료시간."+reservationEndTime);
                            LocalDateTime closeReservationTime= penaltyService.findCloseStratTime(reservationEndTime,penaltiechargerStatusCheckVO.getChargerId());
                            int resNum =  reservation.getReservationId();
                            System.out.println(closeReservationTime);
                            penaltiySendAlarm(session, closeReservationTime, resNum, reservationEndTime);
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
                    num += 1;
                    repeatNum.put("repeat",num);
                    System.out.println("벌금테이블 동작.");
                    LocalDateTime now = LocalDateTime.now();
                    if(now.isBefore(closeReservationTime)) {
                        System.out.println("제일 가까운 예약 내역." + closeReservationTime);

                        String status = String.format("{\"closeReservationTime\": \"%s\", \"message\": \"%s\"}", closeReservationTime, "출차해주세요. 예약시간으로부터 15분 후 보증금 환수 예정.");
                        //뒷차 예약시간을 확인해서 출차해달라고 5분마다 메세지 알람 발송.
                        session.sendMessage(new TextMessage(status));

                    } else if (reservationEndTime.isAfter(reservationEndTime.plusMinutes(15))) {
                        System.out.println("보증금 환수.");
                        String status = String.format("{\"closeReservationTime\": \"%s\", \"message\": \"%s\"}", closeReservationTime, "뒷차 예약시간 부터 1분당 100원 벌금");
                        //
                        session.sendMessage(new TextMessage(status));
                    } else {
                        PenaltyCheckVO vo = penaltyService.penaltyCheckVo(resNum);
                        if(vo == null){
                            penaltyAmount = 100;
                            System.out.println("벌금 부여 시작");
                            PenaltieDTO dto = new PenaltieDTO();
                            dto.setPenaltyAmount(BigDecimal.valueOf(penaltyAmount));
                            dto.setReservationId(resNum); // 보증금 환수.
                            penaltyService.insertPenalty(dto);
                        } else {
                            if(num == 1){
                                penaltyAmount = 100 + vo.getPenaltyAmount();
                                amount.put("amount",penaltyAmount);
                            }else{
                                penaltyAmount += 100;
                                amount.put("amount",penaltyAmount);
                            }
                            penaltyService.updatePenalty(penaltyAmount, resNum);
                        }
                        if(num % 5 == 1) {
                            String status = String.format("{\"reservationId\": \"%d\"}", resNum);
                            session.sendMessage(new TextMessage(status));
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            },0,1,TimeUnit.MINUTES); // 1분마다 실행.
        }
    }