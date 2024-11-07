package com.michael_gregory.blog_api.dto;

import java.time.LocalDateTime;

public class BlogTitleDTO {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BlogTitleDTO(Long id, String title, String author, LocalDateTime createdAt, LocalDateTime updatedAt){
        this.id = id;
        this.title = title;
        this.author = author;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    

}
