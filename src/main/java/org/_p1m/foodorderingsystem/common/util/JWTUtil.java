package org._p1m.foodorderingsystem.common.util;

//import io.lettuce.core.dynamic.annotation.Value;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class JWTUtil {

    private final ServerUtil serverUtil;

    public String extractEmail(String token){
        return Jwts.parserBuilder()
                .setSigningKey(serverUtil.getSecretKey().getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean ValidateToken(String token, UserDetails userDetails){
        return extractEmail(token).equals(userDetails.getUsername());
    }

}