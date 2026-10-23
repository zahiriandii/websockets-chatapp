package com.andi.websocketschatapp.Repository;

import com.andi.websocketschatapp.Model.Message;
import com.andi.websocketschatapp.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long>
{
    User findBySenderId(Long senderId);
    List<Message> findTop50ByRoomNameOrderByCreatedAtDesc(String roomName);
}
