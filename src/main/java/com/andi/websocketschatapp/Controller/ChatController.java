package com.andi.websocketschatapp.Controller;

import com.andi.websocketschatapp.Enum.MessageType;
import com.andi.websocketschatapp.Model.ChatMessage;
import com.andi.websocketschatapp.Service.RoomService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatController
{
    private final RoomService roomService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatController(RoomService roomService, SimpMessagingTemplate simpMessagingTemplate) {
        this.roomService = roomService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

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

    @MessageMapping("/chat.room.{room}")
    public void sendToRoom (@DestinationVariable String room,
                            @Payload ChatMessage chatMessage,
                            Principal principal)
    {
        if (!this.roomService.canAccessRoom(room,principal.getName()))
        {
            return;
        }

        simpMessagingTemplate.convertAndSend("/topic/room/" + room,
                new ChatMessage(MessageType.CHAT,principal.getName(),chatMessage.getContent()));
    }
}
