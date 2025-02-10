package com.michael_gregory.blog_api.service;

import com.michael_gregory.blog_api.entity.users.User;;

public interface UserService {
    User getUser(String username);
    User saveUser(User user);
}



