package com.michael_gregory.blog_api.rest;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.michael_gregory.blog_api.entity.User;
import com.michael_gregory.blog_api.service.UserService;


@RestController
@RequestMapping("/api/user")
public class UserController {

    UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return new ResponseEntity<>(HttpStatus.OK);
	}

    @PostMapping("/register")
	public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	

	@PostMapping("/login")
	public ResponseEntity<String> login(@Valid @RequestBody User user) {
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}
