package com.isep.recommendator.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "app_user")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        allowGetters = true)
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    //TODO unique
    private String email;

    @NotBlank
    private String password;

    private boolean isAdmin;

    public User(){
    }

    public User(String email, String password, boolean isAdmin){
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }


    public String getEmail() {
        return email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getPassword() {
        return password;
    }
}
