package com.michael_gregory.blog_api.rest;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.michael_gregory.blog_api.dto.BlogTitlesResponse;
import com.michael_gregory.blog_api.entity.BlogEntry;
import com.michael_gregory.blog_api.service.BlogEntryService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api")
public class BlogEntryController {

    private BlogEntryService blogEntryService;

    public BlogEntryController(BlogEntryService blogEntryService) {
        this.blogEntryService = blogEntryService;
    }

    // Ex: GET http://localhost:8080/api/findAllTitles?page=0&size20&sort=title,asc
    @GetMapping("/public/find-all-titles")
    public ResponseEntity<BlogTitlesResponse> findAllTitles(Pageable pageable) {
        return blogEntryService.getBlogTitlesPage(pageable);
    }
    
    @GetMapping("/public/blog-entries/{id}")
    public ResponseEntity<BlogEntry> findBlogEntryById( @PathVariable Long id) {
        return blogEntryService.getBlogEntryById(id);
    }

    // Find the newest blog entry.
    // Uses Spring Data REST pagination to fetch only the required record.
    @GetMapping("/public/latest-blog-entry")
    public ResponseEntity<BlogEntry> findNewestBlogEntry() {
        return blogEntryService.findNewestBlogEntry();
    }

    @PostMapping("/admin/blog-entries")
    public ResponseEntity<BlogEntry> createBlogEntry(@Valid @RequestBody BlogEntry blogEntry) {
        return blogEntryService.createBlogEntry(blogEntry);
    }

}
