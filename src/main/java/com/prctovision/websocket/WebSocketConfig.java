package com.prctovision.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebcamWebSocketHandler(), "/ws/webcam")
                .setAllowedOrigins("*"); // Allow all origins (good for local dev, but secure later!)
    }
}
