package com.michael_gregory.blog_api.dto;

import java.util.List;

public class BlogTitlesResponse {
    public List<BlogTitleDTO> blogTitles;
    public int pageCount;

    public BlogTitlesResponse(List<BlogTitleDTO> blogTitleDTOs, int pageCount){
        this.blogTitles = blogTitleDTOs;
        this.pageCount = pageCount;
    }

    public List<BlogTitleDTO> getBlogTitles() {
        return blogTitles;
    }

    public int getPageCount() {
        return pageCount;
    }

}
