package com.andi.websocketschatapp.Service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TokenService
{
    private static final Map<String,String> tokens = Map.of(
            "token-andi","andi",
            "token-bob","bob",
            "token-alice","alice"
    );

    public String validateAndGetToken(String token)
    {
        return tokens.get(token);
    }

}
