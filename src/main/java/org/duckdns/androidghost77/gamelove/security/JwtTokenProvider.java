package org.duckdns.androidghost77.gamelove.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.androidghost77.gamelove.security.dto.UserPrincipal;
import org.springframework.security.core.Authentication;

import java.security.SignatureException;
import java.util.Date;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final String jwtSecret;
    private final int jwtExpirationInMs;

    private static final Map<Class<? extends Exception>, String> EXCEPTION_MESSAGES = Map.of(
            SignatureException.class, "Invalid JWT signature",
            MalformedJwtException.class, "Invalid JWT token",
            ExpiredJwtException.class, "Expired JWT token",
            UnsupportedJwtException.class, "Unsupported JWT token",
            IllegalArgumentException.class, "JWT claims string is empty");

    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date issuedAt = new Date();
        Date expirationDate = new Date(issuedAt.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(userPrincipal.getId())
                .setIssuedAt(issuedAt)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserIdFromJWT(String token) {
        Claims tokenBody = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return tokenBody.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (RuntimeException exc) {
            log.error(EXCEPTION_MESSAGES.get(exc.getClass()), exc);
        }
        return false;
    }
}
