package com.andi.websocketschatapp.Controller.RestController;

import com.andi.websocketschatapp.Model.User;
import com.andi.websocketschatapp.Model.dto.LoginRequest;
import com.andi.websocketschatapp.Model.dto.LoginResponse;
import com.andi.websocketschatapp.Repository.UserRepository;
import com.andi.websocketschatapp.Service.JWTservice.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController
{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn (@RequestBody LoginRequest request)
    {

        return userRepository.findByUsername(request.username())
                .filter(user -> passwordEncoder.matches(user.getPassword(), request.password()))
                .map(user -> ResponseEntity.ok(
                        new LoginResponse(jwtService.generateToken(user.getUsername()), user.getUsername())
                )).orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

    }
}
