package com.example.tnovel_backend.security.jwt;

import com.example.tnovel_backend.repository.user.entity.LocalCredential;
import com.example.tnovel_backend.security.oauth2.principal.OAuth2UserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtProvider implements InitializingBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey key;

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generate(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + (expiration * 1000));
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(auth -> auth.replace("ROLE_", "")) // "ROLE_USER" → "USER"
                .orElse("USER"); // 기본값


        Object principal = authentication.getPrincipal();
        String nickname;

        if (principal instanceof LocalCredential) {
            nickname = ((LocalCredential) principal).getAuthAccount().getUser().getName();
        } else if (principal instanceof OAuth2UserDetails) {
            nickname = ((OAuth2UserDetails) principal).getUsername();
        } else {
            nickname = "UNKNOWN";
        }

        return Jwts.builder()
                .subject(username)
                .claim("nickname", nickname)
                .claim("roles", role)
                .claim(JwtAuthenticationProvider.AUTHORITIES_KEY, authorities)
                .signWith(key)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .compact();
    }




}
