package com.picpaysimplificado.service;

import com.picpaysimplificado.dto.NotificationDTO;
import com.picpaysimplificado.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {
    @Autowired
    private RestTemplate restTemplate;

    public void sendNotification (User user, String message) throws Exception{
       String email = user.getEmail();
       NotificationDTO notificationRequest = new NotificationDTO(email, message);

        ResponseEntity<String> notificationResponse = restTemplate.postForEntity("http://o4d9z.mocklab.io/notify", notificationRequest, String.class);

        //if notification response is not 200, trigger an error
        if (!(notificationResponse.getStatusCode() == HttpStatus.OK)){
            System.out.println("Error ao enviar notificação");
            throw new Exception("O serviço está indisponível ou instável");
        }
    }
}
