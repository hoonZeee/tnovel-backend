package com.example.tnovel_backend.controller.payment.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"imp_key", "imp_secret"})
public class TokenRequestDto {
    @JsonProperty("imp_key")
    private String imp_key;
    @JsonProperty("imp_secret")
    private String imp_secret;
}
