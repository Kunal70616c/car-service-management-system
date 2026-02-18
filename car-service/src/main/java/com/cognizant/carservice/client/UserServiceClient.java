package com.cognizant.carservice.client;

import com.cognizant.carservice.dto.GenericResponse;
import com.cognizant.carservice.security.KeycloakTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserServiceClient {

    private final KeycloakTokenService tokenService;

    @Value("${user.service.url}")
    private String userServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean userExists(Long userId) {

        // 🔥 get internal service token
        String token = tokenService.getServiceToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GenericResponse> response =
                restTemplate.exchange(
                        userServiceUrl + "/users/exists/" + userId,
                        HttpMethod.GET,
                        entity,
                        GenericResponse.class
                );

        if(response.getBody()==null)
            throw new RuntimeException("No response from user-service");

        return (Boolean) response.getBody().getData();
    }
}