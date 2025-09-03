package com.example.tnovel_backend.security.oauth2.handler;

import com.example.tnovel_backend.repository.user.entity.User;
import com.example.tnovel_backend.security.jwt.JwtAuthorizationFilter;
import com.example.tnovel_backend.security.jwt.JwtProvider;
import com.example.tnovel_backend.security.oauth2.principal.OAuth2UserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public static final String REDIRECT_URI = "/set-token";
    public static final String REDIRECT_URL = "http://localhost:8080" + REDIRECT_URI;

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;

        String token = jwtProvider.generate(authentication);

        String redirectUrl = JwtAuthorizationFilter.getRedirectUrl(request, response);

        ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24)
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        response.sendRedirect(redirectUrl);
    }

}
