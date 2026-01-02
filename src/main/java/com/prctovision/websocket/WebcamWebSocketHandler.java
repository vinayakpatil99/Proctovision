package com.prctovision.websocket;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebcamWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("✅ WebSocket Connected: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String frameData = message.getPayload();

        // Example: base64 encoded webcam image frame
        System.out.println("📷 Received frame length: " + frameData.length());

        // TODO:
        // - Save image frame (decode Base64)
        // - Run AI checks: face detection, multiple faces, tab switching, etc.
        // - Possibly forward data to an AI service
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("❌ WebSocket Disconnected: " + session.getId() + " | Reason: " + status);
    }
}
