package com.michael_gregory.blog_api.entity.users;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private boolean enabled = true;

    // One-to-many relationship with authorities
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Authority> authorities;

    // Default constructor
    public User() {
    }

    // Parameterized constructor
    public User(String username, String password, boolean enabled, List<Authority> authorities) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    
}
