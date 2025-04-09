package com.example.newsfeed.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Table(name = "user")
@Getter
public class User extends BaseEntity {

    @Id
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate birthDate;

    private Character gender;

    private String image;

    public User() {

    }

    public User(String email, String password, String nickname, Character gender, String image) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.image = image;
        this.birthDate = LocalDate.now();
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
