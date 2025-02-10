package com.michael_gregory.blog_api.dao.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.michael_gregory.blog_api.entity.users.User;;

public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByUsername(String username);
}