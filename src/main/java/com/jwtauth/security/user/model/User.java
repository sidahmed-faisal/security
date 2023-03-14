package com.jwtauth.security.user.model;

import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue
    private long id;

    @JoinColumn
    @OneToOne(cascade = CascadeType.ALL)
    private  UserDetailsImpl userDetails;


    public User() {
    }
    public User(UserDetailsImpl userDetails) {
        this.userDetails = userDetails;
    }

    public long getId() {
        return id;
    }

    public UserDetailsImpl getUserDetails() {
        return userDetails;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserDetails(UserDetailsImpl userDetails) {
        this.userDetails = userDetails;
    }
}
