package com.renatoconrado.share_books.security.jwt;

import com.renatoconrado.share_books.auth.AuthenticationExceptionHandler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final long JWT_EXPIRATION;
    private final String SECRET_KEY;

    public JwtService(
        @Value("${application.security.jwt.secret-key}") String SECRET_KEY,
        @Value("${application.security.jwt.expiration}") long JWT_EXPIRATION
    ) {
        this.JWT_EXPIRATION = JWT_EXPIRATION;
        this.SECRET_KEY = SECRET_KEY;
    }

    public String extractUsername(String token) {
        return this.extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        return claimResolver.apply(this.extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith((SecretKey) this.getSignInKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = this.extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !this.isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return this.extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return this.extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails) {
        return this.generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
        Map<String, Object> claims,
        UserDetails userDetails
    ) {
        return this.buildToken(new HashMap<>(claims), userDetails, this.JWT_EXPIRATION);
    }

    private String buildToken(
        Map<String, Object> extraClaims,
        UserDetails userDetails,
        long jwtExpiration
    ) {
        var authorities = userDetails.getAuthorities()
            .stream().map(GrantedAuthority::getAuthority).toList();
        extraClaims.put("authorities", authorities);

        var now = Instant.now();

        var builderClaims = Jwts.builder()
            .claims()
            .add(extraClaims)
            .subject(userDetails.getUsername())
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusMillis(jwtExpiration)));

        return builderClaims.and().signWith(this.getSignInKey()).compact();
    }

    /**
     * @throws DecodingException if fail to decode
     * {@link AuthenticationExceptionHandler#handleDecodingException(DecodingException)}
     * @throws WeakKeyException if key is less than 32 bytes
     * {@link AuthenticationExceptionHandler#handleWeakKeyException(WeakKeyException)}
     */
    private Key getSignInKey() throws DecodingException, WeakKeyException {
        byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
