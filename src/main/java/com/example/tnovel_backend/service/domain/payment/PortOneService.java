package com.example.tnovel_backend.service.domain.payment;

import com.example.tnovel_backend.configuration.PortOneConfig;
import com.example.tnovel_backend.controller.payment.dto.request.TokenRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PortOneService {

    private final PortOneConfig portOneConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    public void cancelPayment(String impUid, String reason) {
        String token = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "imp_uid", impUid,
                "reason", reason
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        String url = portOneConfig.getBaseUrl() + "/payments/cancel";

        restTemplate.postForEntity(url, request, String.class);
    }


    public BigDecimal getPaymentAmount(String impUid) {
        String token = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = portOneConfig.getBaseUrl() + "/payments/" + impUid;

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody().get("response");
        return new BigDecimal(responseBody.get("amount").toString());
    }

    public void preparePayment(String merchantUid, BigDecimal amount) {
        String token = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "merchant_uid", merchantUid,
                "amount", amount
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        String url = portOneConfig.getBaseUrl() + "/payments/prepare";

        System.out.println("\n [preparePayment()] called");
        System.out.println("token: " + token);
        System.out.println("merchant_uid: " + merchantUid);
        System.out.println("amount: " + amount);
        System.out.println("POST URL: " + url);
        System.out.println("Request body: " + body);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println(" Response Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
        } catch (Exception e) {
            System.out.println("ERROR while preparing payment");
            e.printStackTrace();
        }
    }


    private String getAccessToken() {
        System.out.println("DEBUG - imp_key: " + portOneConfig.getRest().getApiKey());
        System.out.println("DEBUG - imp_secret: " + portOneConfig.getRest().getApiSecret());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        TokenRequestDto body = new TokenRequestDto(
                portOneConfig.getRest().getApiKey(),
                portOneConfig.getRest().getApiSecret()
        );


        HttpEntity<TokenRequestDto> request = new HttpEntity<>(body, headers);
        String url = portOneConfig.getBaseUrl() + "/users/getToken";

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody().get("response");

        return (String) responseBody.get("access_token");
    }
}
