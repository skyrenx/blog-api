package com.michael_gregory.blog_api.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.michael_gregory.blog_api.entity.User;
import com.michael_gregory.blog_api.exception.EntityNotFoundException;
import com.michael_gregory.blog_api.dao.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
	private PasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User getUser(String username) {
        Optional<User> user = userRepository.findById(username);
        return unwrapUser(user, username);
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    static User unwrapUser(Optional<User> entity, String username) {
        if (entity.isPresent()) return entity.get();
        else throw new EntityNotFoundException(username, User.class);
    }
    
}
