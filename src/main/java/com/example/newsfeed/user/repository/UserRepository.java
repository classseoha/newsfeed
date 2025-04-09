package com.example.newsfeed.user.repository;

import com.example.newsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {


    Optional<User> findByEmail(String email);


    Optional<User> findUserByEmail(String email);


    default User findByIdOrElseThrow(String email){
        return findByEmail(email).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found email= "+ email));
    }
}