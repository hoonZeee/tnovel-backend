package com.example.tnovel_backend.security;

import com.example.tnovel_backend.configuration.PhoneEncryptor;
import com.example.tnovel_backend.repository.user.entity.LocalCredential;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final PhoneEncryptor phoneEncryptor;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        String rawPhoneNumber = token.getName();
        String password = (String) token.getCredentials();

        String encodedPhoneNumber  = phoneEncryptor.encrypt(rawPhoneNumber);

        LocalCredential loggedUser = (LocalCredential) userDetailsService
                .loadUserByUsername(encodedPhoneNumber);

        if (!passwordEncoder.matches(password, loggedUser.getPassword())) {
            throw new BadCredentialsException(loggedUser.getUsername() + " Invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(
                loggedUser,
                null,
                loggedUser.getAuthorities()
        );
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
