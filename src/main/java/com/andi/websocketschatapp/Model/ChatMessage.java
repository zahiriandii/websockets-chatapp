package com.andi.websocketschatapp.Model;

import com.andi.websocketschatapp.Enum.MessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.messaging.handler.annotation.SendTo;

import java.awt.*;

@NoArgsConstructor
@Getter
@SendTo
public class ChatMessage
{
    private MessageType messageType;
    private String sender;
    private String content;
}
