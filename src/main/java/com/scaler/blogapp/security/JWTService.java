package com.scaler.blogapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {
    //TODO:Move the key to a separate .properties file
    private static final String JWT_KEY="asduh^34u87(AS%8HDS&FHid$jgfgn@djn!789";
    private final Algorithm algorithm=Algorithm.HMAC256(JWT_KEY);

    public String createJWT(Long userId){
       return JWT.create()
                .withSubject(userId.toString())
                .withIssuedAt(new Date())
//                .withExpiresAt(new Date()) TODO:set up expiry parameter
                .sign(algorithm);
    }

    public Long retrieveUserId(String jwtString){
        var decodedJWT = JWT.decode(jwtString);
        return Long.valueOf(decodedJWT.getSubject());
    }
}
