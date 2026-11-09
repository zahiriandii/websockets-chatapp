package com.andi.websocketschatapp.Config.DataFiller;

import com.andi.websocketschatapp.Model.Room;
import com.andi.websocketschatapp.Model.User;
import com.andi.websocketschatapp.Repository.RoomRepository;
import com.andi.websocketschatapp.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner
{
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, RoomRepository roomRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() > 0) {
            //already filled with data
            return;
        }
        //!!remember when we add spring security don't use plain passwords, we need to use passwordEncoder
        User andi = userRepository.save(new User ("andi", passwordEncoder.encode("password"), "andi@example.com","andi","zahiri"));
        User bob = userRepository.save(new User ("bob", passwordEncoder.encode("password"), "bob@example.com","bob","bobii"));
        User alice = userRepository.save(new User ("alice", passwordEncoder.encode("password"), "alice@example.com","alice","alicee"));


        Room general = new Room ("general");
        general.getMembers().addAll(List.of(bob, alice,andi));

        Room devs = new Room ("devs");
        devs.getMembers().addAll(List.of(bob,alice));

        Room privatee = new Room ("privatee");
        privatee.getMembers().add(alice);

        roomRepository.saveAll(List.of(general,devs,privatee));

    }
}
