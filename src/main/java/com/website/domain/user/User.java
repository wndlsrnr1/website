package com.website.domain.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id", nullable = false)
    private Long id;

    private String name;

    private String address;

    private String email;

    private String password;

    @Builder
    public User(String name, String address, String email, String password) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.password = password;
    }

}
