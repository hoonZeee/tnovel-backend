package com.example.tnovel_backend.security.oauth2.resource;

import com.example.tnovel_backend.repository.user.entity.vo.Provider;

import java.util.Map;

public interface OAuth2Resource {

    Provider getProvider();
    Long getProviderId();
    Map<String, Object> getAttributes();
    Map<String, Object> getAccount();
    Map<String, Object> getProfile();

}
