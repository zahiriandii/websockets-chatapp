package com.andi.websocketschatapp.Service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class RoomService
{
    private static final Map<String, Set<String>> members = Map.of(
            "general", Set.of("andi","bob","alice"),
            "devs", Set.of("andi","bob"),
            "private",Set.of("alice")
    );

    public boolean canAccessRoom(String room, String username)
    {
        return members.getOrDefault(room, Set.of()).contains(username);
    }

}
