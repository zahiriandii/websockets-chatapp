package com.andi.websocketschatapp.Controller;

import com.andi.websocketschatapp.Enum.MessageType;
import com.andi.websocketschatapp.Model.ChatMessage;
import com.andi.websocketschatapp.Model.Message;
import com.andi.websocketschatapp.Model.Room;
import com.andi.websocketschatapp.Model.User;
import com.andi.websocketschatapp.Repository.MessageRepository;
import com.andi.websocketschatapp.Repository.RoomRepository;
import com.andi.websocketschatapp.Repository.UserRepository;
import com.andi.websocketschatapp.Service.RoomService;
import jakarta.transaction.Transactional;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class ChatController
{
    private final RoomService roomService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public ChatController(RoomService roomService, SimpMessagingTemplate simpMessagingTemplate, MessageRepository messageRepository, UserRepository userRepository, RoomRepository roomRepository) {
        this.roomService = roomService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
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

    @Transactional
    @MessageMapping("/chat.room.{room}")
    public void sendToRoom (@DestinationVariable String room,
                            @Payload ChatMessage chatMessage,
                            Principal principal)
    {
        if (!this.roomService.canAccessRoom(room,principal.getName()))
        {
            simpMessagingTemplate.convertAndSendToUser(principal.getName(),
                    "/queue/errors","Not a memeber of this room" + room);
        }
        User sender = this.userRepository.findByUsername(principal.getName());
        Room room1 = this.roomRepository.findByRoomName(room);

        Message message = new Message(chatMessage.getContent(),sender,room1, LocalDateTime.now());
        messageRepository.save(message);

        simpMessagingTemplate.convertAndSend("/topic/room/" + room,
                new ChatMessage(MessageType.CHAT,principal.getName(),chatMessage.getContent()));
    }
}
