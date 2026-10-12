package com.andi.websocketschatapp.Interceptor;

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

@Component
public class AuthChannelInterceptor implements ChannelInterceptor
{
    private final TokenService tokenService;

    public AuthChannelInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public @Nullable Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

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

        return message;
    }
}
