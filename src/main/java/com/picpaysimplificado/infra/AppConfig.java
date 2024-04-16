package com.picpaysimplificado.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    //indicating to spring which class it has inject to other classes that are using this class as dependency
    @Bean
    public RestTemplate restTemplate () {
        return new RestTemplate();
    }
}
