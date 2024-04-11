package com.picpaysimplificado.repository;

import com.picpaysimplificado.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //method to search users by document, can or not happend 
    Optional<User> findUserByDocument(String document);

    Optional<User> findUserById(Long id);
}
