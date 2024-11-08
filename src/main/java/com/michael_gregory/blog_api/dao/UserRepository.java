package com.michael_gregory.blog_api.dao;

import org.springframework.data.repository.CrudRepository;

import com.michael_gregory.blog_api.entity.User;;

public interface UserRepository extends CrudRepository<User, String> {
	User findByUsername(String username);
}