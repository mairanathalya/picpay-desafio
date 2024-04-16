package com.picpaysimplificado.service;

import com.picpaysimplificado.dto.TransactionDTO;
import com.picpaysimplificado.model.transaction.Transaction;
import com.picpaysimplificado.model.user.User;
import com.picpaysimplificado.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionRepository repository;
    @Autowired
    private RestTemplate restTemplate;  //class to do http communication between services

    public void createTransaction (TransactionDTO transaction) throws Exception{
        //user validations
        User sender = this.userService.findUserById(transaction.senderId());
        User receiver = this.userService.findUserById(transaction.receiverId());

        userService.validateTransaction(sender, transaction.value());

        //return of the authorize transaction if it was not authorized
        boolean isAuthorized = this.authorizeTransaction(sender, transaction.value());
        if (!isAuthorized){
            throw new Exception("Transação não autorizada");
        }

        //creating a transaction
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        //updating users balance
        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        //and saving in DB
        this.repository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);
    }

    //external authorizing service
    public boolean authorizeTransaction (User sender, BigDecimal value){
      ResponseEntity<Map> authrorizationResponse = restTemplate.getForEntity("https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc", Map.class);
      //if the status of the authorization response is status 200, it will compare the body message and return true or false
      if (authrorizationResponse.getStatusCode() == HttpStatus.OK){
          String message = (String) authrorizationResponse.getBody().get("message");
          return "Autorizado".equalsIgnoreCase(message);
      }else
          return false;
    }

}
