package com.andi.websocketschatapp.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne
    private User sender;
    @ManyToOne
    private Room roomName;
    private LocalDateTime createdAt;

    public Message(String content, User sender, Room roomName, LocalDateTime createdAt)
    {
        this.content = content;
        this.sender = sender;
        this.roomName = roomName;
        this.createdAt = createdAt;
    }

}
