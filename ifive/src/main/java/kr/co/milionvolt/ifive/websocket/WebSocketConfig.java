package kr.co.milionvolt.ifive.websocket;

import kr.co.milionvolt.ifive.service.charger.ChargerService;
import kr.co.milionvolt.ifive.service.charging.ChargingStatusSerivce;
import kr.co.milionvolt.ifive.service.penalty.PenaltyService;
import kr.co.milionvolt.ifive.service.reservation.ReservationRedisService;
import kr.co.milionvolt.ifive.service.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final ChargingStatusSerivce chargingStatusSerivce;
    private final UserService userService;
    private final ReservationRedisService reservationRedisService;
    private final ChargerService chargerService;
    private final PenaltyService penaltyService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebSocketConfig(ChargingStatusSerivce chargingStatusSerivce, UserService userService, ReservationRedisService reservationRedisService, ChargerService chargerService, PenaltyService penaltyService, SimpMessagingTemplate simpMessagingTemplate) {
        this.chargingStatusSerivce = chargingStatusSerivce;
        this.userService = userService;
        this.reservationRedisService = reservationRedisService;
        this.chargerService = chargerService;
        this.penaltyService = penaltyService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChargingWebSocketHandler(chargingStatusSerivce), "/charging")
                .setAllowedOrigins("*");

        registry.addHandler(new AlarmWebSocketHandler(userService, reservationRedisService, penaltyService), "/alarm")
                .setAllowedOrigins("*");



    }
}


