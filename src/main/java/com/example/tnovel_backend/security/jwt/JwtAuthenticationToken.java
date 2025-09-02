package com.example.tnovel_backend.security.jwt;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private String principal;
    private String credentials;

    public String getToken(){
        return this.credentials;
    }

    public JwtAuthenticationToken(String token){
        super(null);
        this.credentials = token;
        this.setAuthenticated(false);
    }

    public JwtAuthenticationToken(String subject, Collection<? extends GrantedAuthority> authorities){
        super(authorities);
        this.principal = subject;
        super.setAuthenticated(true);
    }
}
