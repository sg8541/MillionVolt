package kr.co.milionvolt.ifive.websocket;

import kr.co.milionvolt.ifive.domain.notification.ChargingStatusDTO;
import kr.co.milionvolt.ifive.service.charging.ChargingStatusSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChargingWebSocketHandler extends TextWebSocketHandler {
    private final ConcurrentHashMap<String, Double> payMap = new ConcurrentHashMap<>(); // 요금을 계속해서 누적해서 저장하기 위한 Map
    private final ConcurrentHashMap<String, Double> currentBatteryMap = new ConcurrentHashMap<>();// 충전을 계속 누적해서 저장하기 위한 MAP
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private double originCurrentBattery;
    private double chargingSpeed;
    private Integer carId;
    private int batteryPercent;


    private final ChargingStatusSerivce chargingStatusSerivce;

    public ChargingWebSocketHandler(ChargingStatusSerivce chargingStatusSerivce) {
        this.chargingStatusSerivce = chargingStatusSerivce;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String query = session.getUri().getQuery();
        String userId = getParameterFromQuery(query, "userId"); // userId 추출
        if (userId != null) {
            sessions.put(userId, session);
            System.out.println("웹소켓 연결됨. userId: " + userId);
        } else {
            session.close();
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        String payload = message.getPayload();
          System.out.println("메세지 수신 : "+payload);

        if ("start".equals(payload)) {
            startCharging(session);
        } else if ("stop".equals(payload)) {
            stopCharging(session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        String query = session.getUri().getQuery();
        String userId = getParameterFromQuery(query, "userId");
        System.out.println("웹소켓 연결 종료" + userId);
         sessions.remove(userId);
        stopCharging(session);
    }

    private void startCharging(WebSocketSession session) {

        new Thread(() -> {
            try {
                String query = session.getUri().getQuery();
                String userId = getParameterFromQuery(query, "userId");
                int reservationId = Integer.parseInt(getParameterFromQuery(query, "reservationId"));
                ChargingStatusDTO dto = chargingStatusSerivce.chargingStatus(userId, reservationId);
                chargingStatusSerivce.chargingStatusInuse(dto.getChargerId(),dto.getStationId()); // 충전상태로 변환
                double totalBattery = dto.getModelBattery();
                double currentBattery = dto.getCarBattery(); // 현재 배터리 상태
                originCurrentBattery = dto.getCarBattery(); // 초기 현재 배터리 상태
                currentBatteryMap.put(userId, currentBattery);
                carId = dto.getCarId();
                chargingSpeed = getChargingSpeed(dto.getChargerType()); // 몇 kw인지 구분.
                double chargeAmount = chargingSpeed / 3600; // 초당 충전량
                // 초기 배터리 상태를 가져오기 위한 서비스 호출

                while(currentBattery<totalBattery) {
                    if (session == null || !session.isOpen()) {
                        break;
                    }

                    Thread.sleep(1000); // 1초 간격으로 실행.
                    currentBattery += chargeAmount; // chargeAmount에 시간을 곱해야함.


                        if(currentBattery>= totalBattery){
                            currentBattery = totalBattery;
                            chargingStatusSerivce.chargingUpdate(carId,currentBattery);
                            String status = String.format(
                                    "{\"message\": \"충전이 완료되었습니다.\"}"
                            );
                            session.sendMessage(new TextMessage(status));
                            System.out.println("100% 충전완료, 세션 종료.");
                            break; // 충전 완료 시 루프 종료
                        }
                    currentBatteryMap.put(userId, currentBattery);
                    dto.setCarBattery(currentBattery);

                    // 충전 상태를 클라이언트에 전송
                    if (session != null && session.isOpen()) {
                        sendChargingStatus(session, userId, dto);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void sendChargingStatus(WebSocketSession session ,String userId,ChargingStatusDTO dto ) {
       try{
           double chargingKwh = dto.getCarBattery()-originCurrentBattery; // 충전 누적 Kwh
           //double amount =  dto.getTotalPay()*chargingKwh; // 충전중인 요금
           double expectAmount = (dto.getModelBattery()-originCurrentBattery)*dto.getPricePerKWh(); // 예상 총 금액.


           batteryPercent = (int) ((dto.getCarBattery()/dto.getModelBattery())*100); // 충전 퍼센트
           double remainingBattery = dto.getModelBattery() - originCurrentBattery; // 앞으로 충전해야할 배터리량(총베터리량 - 초기 배터리량)
           double estimatedTimeSeconds = remainingBattery / chargingSpeed * 3600; // 예상 시간.

            double oneSecondCharge = expectAmount/estimatedTimeSeconds;
            double amount = payMap.getOrDefault(userId, 0.0);
            amount += oneSecondCharge;
            payMap.put(userId,amount);

           dto.setTotalPay(amount);

           String status = String.format(
                   "{\"batteryPercent\": %d, \"amount\": %.2f, \"chargingKwh\": %.2f, " +
                           "\"chargerType\": \"%s\", \"name\": \"%s\", \"address\": \"%s\", \"userId\": %s, \"username\": \"%s\", " +
                           "\"modelId\": %d, \"reservationId\": %d, \"stationId\": %d, \"carNumber\": \"%s\",\"pricePerKWh\": %.2f, " +
                           " \"expectAmount\": %.2f , \"estimatedTimeSeconds\": %.2f , \"message\": \"%s\"}",
                   batteryPercent, dto.getTotalPay(), chargingKwh,
                   dto.getChargerType(), dto.getName(), dto.getAddress(), dto.getId(),
                   dto.getUsername(), dto.getModelId(), dto.getReservationId(), dto.getStationId(), dto.getCarNumber(),
                   dto.getPricePerKWh(),expectAmount,estimatedTimeSeconds, dto.getMessage());
           session.sendMessage(new TextMessage(status));
       }catch (Exception e){
           e.printStackTrace();
       }

    }




    private void stopCharging(WebSocketSession session) {
        try {
            String query = session.getUri().getQuery();
            String userId = getParameterFromQuery(query, "userId");


            if (userId != null) {
                Double currentBattery = currentBatteryMap.get(userId);
                if (currentBattery != null) {
                    // 현재 배터리 상태 업데이트
                    chargingStatusSerivce.chargingUpdate(carId, currentBattery);
                    currentBatteryMap.remove(userId); // 맵에서 제거
                }
                // WebSocket 세션 종료
                sessions.remove(userId);
                session.close();
                System.out.println("충전 중단 및 웹소켓 종료됨: " + userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getParameterFromQuery(String query, String key) {
        if (query == null || query.isEmpty()) {
            return null;
        }
        String[] params = query.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2 && keyValue[0].equals(key)) {
                return keyValue[1];
            }
        }
        return null;
    }

    private double getChargingSpeed(String chargerType) {
        switch (chargerType) {
            case "300kw":
                return 300.0;
            case "200kw":
                return 200.0;
            case "100kw":
                return 100.0;
            case "50kw":
                return 50.0;
            default:
                return 7.0;
        }
    }


}
