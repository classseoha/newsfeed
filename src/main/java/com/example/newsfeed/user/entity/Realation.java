package com.example.newsfeed.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "realation")
@Getter
@NoArgsConstructor
public class Realation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User followingEmail;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User followedEmail;


    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

}
