package com.example.tnovel_backend.repository.user.entity.vo;

import java.util.Arrays;

public enum Provider {
    LOCAL,
    KAKAO,
    NAVER,
    GOOGLE,
    APPLE;

    public static Provider findByName(String name){
        return Arrays.stream(Provider.values())
                .filter((each)->each.name().equals(name.toUpperCase()))
                .findFirst()
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 Source 검색 : " + name.toUpperCase()));
    }
}
