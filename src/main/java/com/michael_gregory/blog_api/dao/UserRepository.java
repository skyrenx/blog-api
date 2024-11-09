package com.michael_gregory.blog_api.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.michael_gregory.blog_api.entity.User;;

public interface UserRepository extends CrudRepository<User, String> {
	Optional<User> findByUsername(String username);
}