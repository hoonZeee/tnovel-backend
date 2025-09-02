package com.example.tnovel_backend.security.oauth2.resource;

import com.example.tnovel_backend.repository.user.entity.vo.Provider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2KakaoResource implements OAuth2Resource {
    private final Provider provider;
    private final Long providerId;
    private final Map<String,Object> attributes;
    private final Map<String, Object> account;
    private final Map<String, Object> profile;

    public static OAuth2KakaoResource create(OAuth2User resourceResponse){
        final Map<String, Object> attributes = resourceResponse.getAttributes();
        final Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");

        Object id = attributes.get("id");
        Long providerId = (id instanceof Number) ? ((Number) id).longValue() : Long.valueOf(id.toString());

        return new OAuth2KakaoResource(
                Provider.KAKAO,
                providerId,
                attributes,
                account,
                (Map<String, Object>) account.get("profile")
        );
    }
}
