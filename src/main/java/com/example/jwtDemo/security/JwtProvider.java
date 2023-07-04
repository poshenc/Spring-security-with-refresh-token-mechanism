package com.example.jwtDemo.security;

import com.example.jwtDemo.repository.RefreshTokenRepository;
import com.example.jwtDemo.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private static final String SECRET = "jwt demo";
//    private final RefreshTokenRepository refreshTokenRepository;
//    private final UserRepository userRepository;



    public static String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName, "access");
    }

//    public static String refreshToken(String userName) {
//        Map<String, Object> claims = new HashMap<>();
//        return createToken(claims, userName, "refresh");
//    }

    private static String createToken(Map<String, Object> claims, String userName, String type) {
        long numberOfMinutes = Objects.equals(type, "access") ? 1L : 5 *60L;
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 / 10  * numberOfMinutes))
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();
    }


    //extract claims
    public static String extractUserName(String token) throws Exception{
        return extractClaim(token, Claims::getSubject);
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();

    }

    //validate token
    public static Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUserName(token);
            return username.equals(userDetails.getUsername());
        }catch (Exception exception){
            return false;
        }
    }
}
