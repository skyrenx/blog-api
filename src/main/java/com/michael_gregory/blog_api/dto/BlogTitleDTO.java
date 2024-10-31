package com.michael_gregory.blog_api.dto;

public class BlogTitleDTO {
    private Long id;
    private String title;

    public BlogTitleDTO(Long id, String title){
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    

}
