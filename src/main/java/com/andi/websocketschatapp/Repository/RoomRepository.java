package com.andi.websocketschatapp.Repository;

import com.andi.websocketschatapp.Model.Room;
import com.andi.websocketschatapp.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface RoomRepository extends JpaRepository<Room,Long>
{
    List<Room> findByRoomName(String roomName);
    /** Finds Members by username using the Room_Users table */
    List<Room> findByMembersUsername(String username);
}
