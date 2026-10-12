package com.andi.websocketschatapp.Controller;

import com.andi.websocketschatapp.Enum.MessageType;
import com.andi.websocketschatapp.Model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatController
{
    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, Principal principal)
    {
        return new ChatMessage(MessageType.CHAT,principal.getName(),chatMessage.getContent());
    }

    @MessageMapping("/chat.join")
    @SendTo("/topic/public")
    public ChatMessage join (Principal principal )
    {
        //headerAccessor.getSessionAttributes().put("username",chatMessage.getSender());
        return new ChatMessage(MessageType.JOIN, principal.getName(),null);
    }
}
