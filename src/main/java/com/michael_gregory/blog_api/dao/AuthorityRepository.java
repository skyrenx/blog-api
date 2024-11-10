package com.michael_gregory.blog_api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.michael_gregory.blog_api.entity.Authority;
import com.michael_gregory.blog_api.entity.AuthorityId;

@RepositoryRestResource(exported = false)
public interface AuthorityRepository extends CrudRepository<Authority, AuthorityId> {
     @Query("SELECT a FROM Authority a WHERE a.user.username = :username")
    List<Authority> findByUserUsername(String username);

}
