package com.treinetic.TaskManager.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwt.access.secret}")
    private String accessTokenSecret;

    @Value("${jwt.refresh.secret}")
    private String refreshTokenSecret;

    private long accessTokenExpiration = 1000 * 60 * 2; //2min
    private long refreshTokenExpiration = 1000 * 60 * 60 * 24 * 7;  //7d

    public String generateAccessToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(generateKey(accessTokenSecret))
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(generateKey(refreshTokenSecret))
                .compact();
    }

    //to extract user email
    public String extractUsername(String token, boolean isRefreshToken) {
        String secret = isRefreshToken ? refreshTokenSecret : accessTokenSecret;
        return extractClaim(token, secret, Claims::getSubject);
    }
    public UserDetails extractUserDetails(String token, boolean isRefreshToken) {
        String secret = isRefreshToken ? refreshTokenSecret : accessTokenSecret;
        Claims claims = extractAllClaims(token, secret);

        return new User(claims.getSubject(), "", List.of());
    }
    public boolean isTokenValid(String token, UserDetails userDetails, boolean isRefreshToken) {
        final String username = extractUsername(token, isRefreshToken);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token, isRefreshToken);
    }
    //to extract a single claim
    public <T> T extractClaim(String token, String secret, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token, secret);
        return claimsResolver.apply(claims);
    }
    public Claims extractAllClaims(String token, String secret) {
        try {
            return Jwts.parser()
                    .verifyWith(generateKey(secret)).build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            //need to catch the 1st 403 code here in the frontend to send the req to the refresh token end point
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "jwt expired");
        } catch (SignatureException e) {
            //after catching the 1st 403 res and then again if it sends 403 here,
            //user should be logged out from the frontend cuz that means token has been tampered
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invalid token");
        } catch (JwtException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "error validating token: " + e.getMessage());
        }
    }

    private boolean isTokenExpired(String token, boolean isRefreshToken) {
        String secret = isRefreshToken ? refreshTokenSecret : accessTokenSecret;
        return extractExpiration(token, secret).before(new Date());
    }
    private Date extractExpiration(String token, String secret) {
        return extractClaim(token, secret, Claims::getExpiration);
    }
    private SecretKey generateKey(String secret) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }
}
