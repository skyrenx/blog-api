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
@RequestMapping("/api")
public class UserController {

    UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/admin/user/{id}")
	public ResponseEntity<String> findByIdAdmin(@PathVariable String id) {
		String username = userService.getUser(id).getUsername();
		return new ResponseEntity<>("admin: " + username, HttpStatus.OK);
	}
	@GetMapping("/user/{id}")
	public ResponseEntity<String> findById(@PathVariable String id) {
		String username = userService.getUser(id).getUsername();
		return new ResponseEntity<>("user: " + username, HttpStatus.OK);
	}	

    @PostMapping("/public/user/register")
	public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
		userService.saveUser(user);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	

	//TODO this should be an authentication endpoint, like /authenticate
	@PostMapping("/user/login")
	public ResponseEntity<String> login(@Valid @RequestBody User user) {
		return new ResponseEntity<>("Login success!", HttpStatus.ACCEPTED);
	}
}
