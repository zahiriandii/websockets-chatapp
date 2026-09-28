package com.andi.websocketschatapp.Model;

import com.andi.websocketschatapp.Enum.MessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.messaging.handler.annotation.SendTo;

import java.awt.*;

@NoArgsConstructor
@Getter
@Setter
public class ChatMessage
{
    private MessageType type;
    private String sender;
    private String content;
}
