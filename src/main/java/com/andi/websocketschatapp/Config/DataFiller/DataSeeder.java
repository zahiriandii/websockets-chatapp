package com.andi.websocketschatapp.Config.DataFiller;

import com.andi.websocketschatapp.Model.Room;
import com.andi.websocketschatapp.Model.User;
import com.andi.websocketschatapp.Repository.RoomRepository;
import com.andi.websocketschatapp.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner
{
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public DataSeeder(UserRepository userRepository, RoomRepository roomRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() > 0) {
            //already filled with data
            return;
        }

        User andi = userRepository.save(new User ("andi", "password", "andi@example.com","andi","zahiri"));
        User bob = userRepository.save(new User ("bob", "password", "bob@example.com","bob","bobii"));
        User alice = userRepository.save(new User ("alice", "password", "alice@example.com","alice","alicee"));


        Room general = new Room ("general");
        general.getMembers().addAll(List.of(bob, alice,andi));

        Room devs = new Room ("devs");
        devs.getMembers().addAll(List.of(bob,alice));

        Room privatee = new Room ("privatee");
        privatee.getMembers().add(alice);

        roomRepository.saveAll(List.of(general,devs,privatee));

    }
}
