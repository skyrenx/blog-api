package com.michael_gregory.blog_api.dto;

import java.util.List;

public class BlogTitlesResponse {
    public List<BlogTitle> blogTitles;
    public int pageCount;

    public BlogTitlesResponse(List<BlogTitle> blogTitleDTOs, int pageCount){
        this.blogTitles = blogTitleDTOs;
        this.pageCount = pageCount;
    }

    public List<BlogTitle> getBlogTitles() {
        return blogTitles;
    }

    public int getPageCount() {
        return pageCount;
    }

}
