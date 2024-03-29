package com.isep.recommendator.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    public  String name;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    private String lastName;
    private String firstName;
    private String employeeType;
    private String employeeNumber;

    @Email
    private String email;

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    @JsonIgnore
    private String password;

    private boolean isAdmin;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private Set<History> histories = new HashSet<>();

    public User() {
    }

    public User(String username, String password, String name, String lastName, String firstName, String type, String numero, String email) {
        this.name = name;
        this.lastName = lastName;
        this.firstName = firstName;
        this.employeeType = type;
        this.employeeNumber = numero;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }


    public String toString() {
        return "username = " + username + " name = " + name + " type = " + employeeType + " id = " + employeeNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getPassword() {
        return password;
    }

    public Long getId() {
        return user_id;
    }

    public void setId(Long user_id) {
        this.user_id = user_id;
    }

    public Set<History> getHistories() {
        return histories;
    }

    public void setHistories(Set<History> histories) {
        this.histories = histories;
    }

    public void addHistory(History history) {
        this.histories.add(history);
    }

    public String getEmployeeType() {
        return employeeType;
    }

}
