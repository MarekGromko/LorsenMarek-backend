package edu.lorsenmarek.backend.util;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.lang.SecurityException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * Utility class to handle JWT
 */
@Component
public class JwtUtil {
    final private String JWT_SECRET;
    final private Duration JWT_EXPIRATION;
    private SecretKey key;

    /**
     * Create a new {@link JwtUtil}
     * @param jwtSecret depends on props <code>auth.jwt.secret</code>
     * @param jwtExpiration depends on props <code>auth.jwt.expiration</code>
     */
    JwtUtil(
            @Value("${auth.jwt.secret}") final String jwtSecret,
            @Value("${auth.jwt.expiration}") final String jwtExpiration
    ) {
        JWT_SECRET = jwtSecret;
        JWT_EXPIRATION = DurationCodecUtil.decode(jwtExpiration);
    }
    /** Initialize the secret key */
    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generate a new JWT given a subject
     * @param subject the subject to pass in the JWT
     * @return the computed JWT
     */
    public String generateToken(String subject){
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(JWT_EXPIRATION)))
                .signWith(key)
                .compact();
    }

    /**
     * Get the subject (sub) from a JWT
     * @param raw_token the raw string JWT token
     * @return the token's subject
     */
    public String getSubjectFromToken(String raw_token){
        return Jwts.parser()
                .verifyWith(key).build()
                .parseSignedClaims(raw_token)
                .getPayload()
                .getSubject();
    }
    /**
     * Validate a JWT token
     * @param token the token
     * @return if the token is valid
     */
    public boolean validateJwtToken(String token){
        try{
            Jwts.parser().verifyWith(key).build().parse(token);
            return true;
        } catch (SecurityException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }
}