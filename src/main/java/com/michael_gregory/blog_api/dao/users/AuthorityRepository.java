package com.michael_gregory.blog_api.dao.users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.michael_gregory.blog_api.entity.users.Authority;
import com.michael_gregory.blog_api.entity.users.AuthorityId;

public interface AuthorityRepository extends JpaRepository<Authority, AuthorityId> {
     @Query("SELECT a FROM Authority a WHERE a.user.username = :username")
    List<Authority> findByUserUsername(String username);

    // TODO Consider the following before implementing a DELETE method.
    // Before doing a DELETE, verify that the User exists with the Authority's username 
}
