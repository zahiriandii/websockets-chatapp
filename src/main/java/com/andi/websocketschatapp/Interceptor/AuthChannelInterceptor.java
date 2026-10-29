package com.andi.websocketschatapp.Interceptor;

import com.andi.websocketschatapp.Repository.RoomRepository;
import com.andi.websocketschatapp.Repository.UserRepository;
import com.andi.websocketschatapp.Service.RoomService;
import com.andi.websocketschatapp.Service.TokenService;
import org.jspecify.annotations.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class AuthChannelInterceptor implements ChannelInterceptor
{
    private final TokenService tokenService;
    private final RoomRepository roomRepository;
    //private final RoomService roomService;

    public AuthChannelInterceptor(TokenService tokenService, RoomRepository roomRepository) {
        this.tokenService = tokenService;
        //this.roomService = roomService;
        this.roomRepository = roomRepository;
    }

    @Override
    public @Nullable Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        //Authentication Branch
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand()))
        {
            String token = accessor.getFirstNativeHeader("Authorization");
            String username = tokenService.validateAndGetToken(token);
            if (username==null)
            {
                throw new MessageDeliveryException("Invalid token");
            }
            accessor.setUser(()->username);

        }

        //Subscribe branch (checks if a user has access to a private room)
        if (accessor != null && StompCommand.SUBSCRIBE.equals(accessor.getCommand()))
        {
            String destination = accessor.getDestination();
            Principal user = accessor.getUser();
            String ROOM_PREFIX = "/topic/room/";
            if (destination == null)
            {
                throw new MessageDeliveryException("Invalid destination");
            }

            if (!destination.startsWith(ROOM_PREFIX))
            {
                return message;
            }

            String room = destination.substring(ROOM_PREFIX.length());
            if (user == null || !roomRepository.isMember(room,user.getName()))
            {

                throw new MessageDeliveryException("Access denied");
            }

        }

        return message;
    }
}
