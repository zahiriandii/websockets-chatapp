package com.andi.websocketschatapp.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Message
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne
    private User sender;
    @ManyToOne
    private Room room;
    private LocalDateTime sentAt;
}
