package com.andi.websocketschatapp.Listener;

import com.andi.websocketschatapp.Enum.MessageType;
import com.andi.websocketschatapp.Model.ChatMessage;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@Component
public class WebSocketEventListener
{
    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebSocketEventListener(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    @EventListener
    public void handleConnect(SessionConnectedEvent event) {
        System.out.println("CONNECTED: " + StompHeaderAccessor.wrap(event.getMessage()).getSessionId());
    }

    @EventListener
    public void handleDisconnect (SessionDisconnectEvent disconnectEvent) {
        StompHeaderAccessor accessor =  StompHeaderAccessor.wrap(disconnectEvent.getMessage());
        Map<String ,Object> attributes = accessor.getSessionAttributes();

        if (attributes == null)
            return;

        String username = (String) attributes.get("username");
        if (username == null)
            return;

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(MessageType.LEAVE);
        chatMessage.setSender(username);

        simpMessagingTemplate.convertAndSend("/topic/public", chatMessage);
        System.out.println("DISCONNECTED: " + StompHeaderAccessor.wrap(disconnectEvent.getMessage()).getSessionId());
    }
}
