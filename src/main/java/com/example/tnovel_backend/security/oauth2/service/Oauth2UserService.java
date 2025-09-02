package com.example.tnovel_backend.security.oauth2.service;

import com.example.tnovel_backend.repository.user.AuthAccountRepository;
import com.example.tnovel_backend.repository.user.UserRepository;
import com.example.tnovel_backend.repository.user.entity.AuthAccount;
import com.example.tnovel_backend.repository.user.entity.User;
import com.example.tnovel_backend.repository.user.entity.vo.Provider;
import com.example.tnovel_backend.security.oauth2.principal.OAuth2UserDetails;
import com.example.tnovel_backend.security.oauth2.resource.OAuth2KakaoResource;
import com.example.tnovel_backend.security.oauth2.resource.OAuth2Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class Oauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final AuthAccountRepository authAccountRepository;

    private OAuth2Resource extract(String provider, OAuth2User resourceResponse){
        return switch(Provider.findByName(provider)) {
            case KAKAO -> OAuth2KakaoResource.create(resourceResponse);
            default -> throw new IllegalArgumentException("존재하지 않은 RegistrationId : " + provider);
        };
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest resourceRequest) throws OAuth2AuthenticationException {
        final OAuth2User resourceResponse = super.loadUser(resourceRequest);
        final OAuth2Resource resource = extract(
                resourceRequest.getClientRegistration().getRegistrationId(),
                resourceResponse
        );

        String providerUserId = resource.getProviderId().toString();
        Provider provider = resource.getProvider();

        AuthAccount authAccount = authAccountRepository
                .findByProviderAndProviderUserId(provider, providerUserId)
                .orElse(null);

        User user;
        if (authAccount != null) {
            user = authAccount.getUser();
        } else {
            user = userRepository.save(User.create(resource));
            AuthAccount newAuthAccount = AuthAccount.create(user, providerUserId, provider);
            authAccountRepository.save(newAuthAccount);
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                "",
                List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );

        return OAuth2UserDetails.create(resource, userDetails);

    }

}
