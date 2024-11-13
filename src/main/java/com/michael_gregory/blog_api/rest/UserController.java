package com.michael_gregory.blog_api.rest;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.michael_gregory.blog_api.entity.User;
import com.michael_gregory.blog_api.security.SecurityConstants;
import com.michael_gregory.blog_api.service.UserService;


@RestController
@RequestMapping("/api")
public class UserController {

    UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	// TODO I tried this annotation @PreAuthorize("hasRole('ADMIN')")
	// but users without the correct role were still able to access the endpoint
	// Configuring the endpoint to require admin role in securityConfig is working.
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
	//Processed by AuthenticationFilter
    //@PostMapping("/api/public/user/login")
    
	// @GetMapping("/user/name")
	// public String getMethodName(@RequestParam String param) {
	// 	return new String();
	// }
	

}
