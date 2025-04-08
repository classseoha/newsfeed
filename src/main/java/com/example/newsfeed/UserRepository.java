package com.example.newsfeed;

import com.example.newsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, String> {

    Optional<User> findUserByEmail(String email);

    default User findUserByEmailOrElseThrow(String email){
        return findById(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
