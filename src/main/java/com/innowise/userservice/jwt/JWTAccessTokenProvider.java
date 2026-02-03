package com.innowise.userservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JWTAccessTokenProvider {
        @Value("${jwt.access.secret}")
        private String jwtSecret;
        @Value("${jwt.access.expiration}")
        private long jwtExpirationInMs;

        private SecretKey key;
        @PostConstruct
        public void init() {
            this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        }

        public Authentication getAuthentication(String token) {
            Long userIdFromJWT = getUserIdFromJWT(token);
            GrantedAuthority role = new SimpleGrantedAuthority(getUserRolesFromJWT(token));
            return new UsernamePasswordAuthenticationToken(userIdFromJWT, token, List.of(role));
        }

        public String resolveToken(HttpServletRequest req) {
            String bearerToken = req.getHeader("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }
            return null;
        }

        public boolean validateToken(String token) {
            try {
                Claims claims = Jwts.parser().verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                return !claims.getExpiration().before(new Date());
            } catch (JwtException | IllegalArgumentException e) {
                return false;
            }
        }

        public Long getUserIdFromJWT(String token) {
            return Jwts.parser().
                    verifyWith(key).
                    build().
                    parseSignedClaims(token).
                    getPayload().
                    get("id", Long.class);
        }

        public String getUserRolesFromJWT(String token) {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("role",String.class);
        }

    }
