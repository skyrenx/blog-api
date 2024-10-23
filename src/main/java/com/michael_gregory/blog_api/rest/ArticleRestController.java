package com.michael_gregory.blog_api.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class ArticleRestController {
    
    @GetMapping("/articles")
    public List<String> findAll(){
        List<String> articles = new ArrayList<>();
        articles.add("first article");
        articles.add("second article");
        return articles;
    }
}
