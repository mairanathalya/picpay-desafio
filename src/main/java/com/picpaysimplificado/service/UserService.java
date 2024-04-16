package com.picpaysimplificado.service;

import com.picpaysimplificado.model.user.User;
import com.picpaysimplificado.model.user.UserType;
import com.picpaysimplificado.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public void validateTransaction (User sender, BigDecimal amount) throws  Exception{
        //validation based on user types to know if they are able to make a transaction
        if (sender.getUserType() == UserType.MERCHANT){
            throw new Exception("Usuário do tipo Lojista não está autorizado a relizar transação");
        }
        //check if have balance to make a transfer
        if (sender.getBalance().compareTo(amount) < 0){
            throw new Exception("Saldo insuficiente");
        }
    }

    //method to allow the transaction service to manipulate users without needing the repository user
    public User findUserById(Long id) throws Exception{
        return this.repository.findUserById(id).orElseThrow( () ->new Exception("Usuário não encontrado"));
    }

    public void saveUser (User user){
        this.repository.save(user);
    }
    
}
