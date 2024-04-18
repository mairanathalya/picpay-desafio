package com.picpaysimplificado.service;

import com.picpaysimplificado.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class AuthorizationService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.authorizationApi}")
    private String authApiUrl;

    //external authorizing service
    public boolean authorizeTransaction (User sender, BigDecimal value){
        ResponseEntity<Map> authrorizationResponse = restTemplate.getForEntity(this.authApiUrl, Map.class);
        //if the status of the authorization response is status 200, it will compare the body message and return true or false
        if (authrorizationResponse.getStatusCode() == HttpStatus.OK){
            String message = (String) authrorizationResponse.getBody().get("message");
            return "Autorizado".equalsIgnoreCase(message);
        }else
            return false;
    }
}
