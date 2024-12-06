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
    private final ConcurrentHashMap<String, Double> payMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Double> currentBatteryMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    double originCurrentBattery;

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
            System.out.println("userId가 누락되었습니다.");
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
          //  stopCharging(session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        String query = session.getUri().getQuery();
        String userId = getParameterFromQuery(query, "userId");
        System.out.println("웹소켓 연결 종료" + userId);
         sessions.remove(userId);// 세션 유지 무조건 종료를 눌렀을때만 종료.

    }


    private void startCharging(WebSocketSession session) {

        new Thread(() -> {
            try {
                String query = session.getUri().getQuery();
                String userId = getParameterFromQuery(query, "userId");
                ChargingStatusDTO dto = chargingStatusSerivce.chargingStatus(userId, 1);
                double totalBattery = dto.getModelBattery();
                double currentBattery = dto.getCarBattery(); // 현재 배터리 상태
                originCurrentBattery =dto.getCarBattery(); // 초기 현재 배터리 상태
                currentBatteryMap.put(userId, currentBattery);
                System.out.println("1 : "+currentBattery);

                double pay = dto.getPricePerKWh()/3600;
                payMap.put(userId, pay);
                double chargingSpeed = getChargingSpeed(dto.getChargerType()); // 몇 kw인지 구분.
                double chargeAmount = chargingSpeed / 3600; // 초당 충전량
                // 초기 배터리 상태를 가져오기 위한 서비스 호출

                while(currentBattery<totalBattery) {
                    Thread.sleep(1000); // 1초 간격으로 실행.
                    currentBattery += chargeAmount; // chargeAmount에 시간을 곱해야함.


                        if(currentBattery>= totalBattery){
                            currentBattery = totalBattery;


                            // afterConnectionClosed(session);
                        }
                    currentBatteryMap.put(userId, currentBattery);
                    dto.setCarBattery(currentBattery);



                    double totalPay = (payMap.get(userId)+pay);

                    System.out.println("확인 : ㅇㅇ "+payMap.get(userId));

                    payMap.put(userId,totalPay);
                    dto.setTotalPay(totalPay);

                    // 충전 상태를 클라이언트에 전송
                    sendChargingStatus(session, userId, dto);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }



    private void sendChargingStatus(WebSocketSession session ,String userId,ChargingStatusDTO dto ) {
       try{
           System.out.println("총 배터리 :"+dto.getModelBattery()); // 총 배터리
           System.out.println("현재 배터리 : "+dto.getCarBattery()); // 현재 배터리
           System.out.println(dto.getCarBattery());

           double chargingKwh = dto.getCarBattery()-originCurrentBattery;
           double amount =  (dto.getTotalPay()*(chargingKwh)); // 충전중인 요금
           System.out.println(amount+"원");
           
           int batteryPercent = (int) ((dto.getCarBattery()/dto.getModelBattery())*100); // 충전 퍼센트

           System.out.println(batteryPercent+"%");

           String status = String.format(
                   "{\"batteryPercent\": %d, \"amount\": %.2f, \"chargingKwh\": %.2f, " +
                           "\"chargerType\": \"%s\", \"name\": \"%s\", \"address\": \"%s\", \"userId\": %s, \"username\": \"%s\", " +
                           "\"modelId\": %d, \"reservationId\": %d, \"stationId\": %d}",
                   batteryPercent, amount, chargingKwh,
                   dto.getChargerType(), dto.getName(), dto.getAddress(), dto.getId(),
                   dto.getUsername(), dto.getModelId(), dto.getReservationId(), dto.getStationId());
           session.sendMessage(new TextMessage(status));
       }catch (Exception e){
           e.printStackTrace();
       }

    }



//
//    private void stopCharging(WebSocketSession session) {
//        try {
//            String query = session.getUri().getQuery();
//            String userId = getParameterFromQuery(query, "userId");
//
//            if (userId != null && sessions.containsKey(userId)) {
//                sessions.remove(userId);
//                session.close();
//                System.out.println("웹소켓 연결 종료됨: " + userId);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


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
