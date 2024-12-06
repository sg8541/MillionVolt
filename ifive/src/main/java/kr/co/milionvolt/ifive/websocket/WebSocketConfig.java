package kr.co.milionvolt.ifive.websocket;

import kr.co.milionvolt.ifive.service.charging.ChargingStatusSerivce;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChargingStatusSerivce chargingStatusSerivce;

    public WebSocketConfig(ChargingStatusSerivce chargingStatusSerivce) {
        this.chargingStatusSerivce = chargingStatusSerivce;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChargingWebSocketHandler(chargingStatusSerivce), "/charging")
                .setAllowedOrigins("*");
    }
}
