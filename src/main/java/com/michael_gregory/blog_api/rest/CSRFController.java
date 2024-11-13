package com.michael_gregory.blog_api.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;

//TODO probably don't need this controller at all, delete
@RestController
@RequestMapping("/api")
public class CSRFController {

    @GetMapping("/public/csrf")
    //public CsrfToken getMethodName(CsrfToken token) {
    public String getMethodName(){
        return "";
    }
    
}
