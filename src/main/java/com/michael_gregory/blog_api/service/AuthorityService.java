package com.michael_gregory.blog_api.service;

import java.util.List;

import com.michael_gregory.blog_api.entity.Authority;

public interface AuthorityService {
    List<Authority> getAuthorities(String username);
}
