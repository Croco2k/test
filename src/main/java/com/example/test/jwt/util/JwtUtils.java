package com.example.test.jwt.util;

import com.example.test.model.entity.User;
import com.example.test.model.enums.RoleName;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class JwtUtils {


    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    private final static String ROLE = "ROLE";

    public String generateToken(User user) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(ROLE, user.getRole().getName());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

    }

    public boolean validateToken(String token, User user) {
        final String login = getLoginFromToken(token);
        final RoleName role = getRoleFromToken(token);
        return (login.equals(user.getUsername()) && role.equals(user.getRole().getName()) && !isTokenExpired(token));
    }

    public String getLoginFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public RoleName getRoleFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        if (claims.containsKey(ROLE)) {
            return RoleName.valueOf((String) claims.get(ROLE));
        } else {
            throw new IllegalArgumentException("Token does not contain ROLE claim");
        }
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getExpiration();
    }
}
