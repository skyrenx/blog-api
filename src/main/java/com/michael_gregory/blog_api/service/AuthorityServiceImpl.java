package com.michael_gregory.blog_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.michael_gregory.blog_api.dao.AuthorityRepository;
import com.michael_gregory.blog_api.entity.Authority;

@Service
public class AuthorityServiceImpl implements AuthorityService{

    private AuthorityRepository authorityRepository;

    public AuthorityServiceImpl(AuthorityRepository authorityRepository){
        this.authorityRepository = authorityRepository;
    }

    @Override
    public List<Authority> getAuthorities(String username) {

        // Fetch authorities by the composite key (AuthorityId)
        List<Authority> authorities = authorityRepository.findByUserUsername(username);
        return authorities;
    }

}
