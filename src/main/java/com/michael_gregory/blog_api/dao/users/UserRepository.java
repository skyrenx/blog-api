package com.michael_gregory.blog_api.dao.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.michael_gregory.blog_api.entity.User;;

public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByUsername(String username);
}