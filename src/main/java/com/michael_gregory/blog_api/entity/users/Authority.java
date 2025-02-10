package com.michael_gregory.blog_api.entity.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "authorities")
@IdClass(AuthorityId.class)
public class Authority {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="username", referencedColumnName = "username", insertable = false, updatable = false)
    private User user;

    // Foreign key to User (mapped by username)
    @Id
    @Column(name = "username", length = 50, nullable = false)
    @NotBlank(message = "username cannot be blank")
    private String username;

    @Id
    @Column(name = "authority", length = 50, nullable = false)
    @NotBlank(message = "authority cannot be blank")
    private String authority;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    
}
