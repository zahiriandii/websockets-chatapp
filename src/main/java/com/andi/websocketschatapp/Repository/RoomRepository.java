package com.andi.websocketschatapp.Repository;

import com.andi.websocketschatapp.Model.Room;
import com.andi.websocketschatapp.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface RoomRepository extends JpaRepository<Room,Long>
{
    List<Room> findByRoomName(String roomName);
    /** Finds Members by username using the Room_Users table */
    List<Room> findByMembersUsername(String username);

    @Query("select count(r)>0 from Room r join r.members m " +
            " where r.roomName = :room and m.username = :username ")
    boolean isMember (@Param("room") String room, @Param("username") String username);
}
