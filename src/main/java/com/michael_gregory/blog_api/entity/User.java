package com.michael_gregory.blog_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "username", length = 50, nullable = false)
	@NotBlank(message = "username cannot be blank")
    private String username;

    @Column(name = "password", nullable = false, length = 68)
	@NotBlank(message = "password cannot be blank")
    private String password;

    @Column(name = "enabled", nullable = false)
    private int enabled = 1;

    // Default constructor
    public User() {
    }

    // Parameterized constructor
    public User(String username, String password, int enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int isEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }
}
