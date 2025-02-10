package com.michael_gregory.blog_api.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.michael_gregory.blog_api.dto.BlogTitlesResponse;
import com.michael_gregory.blog_api.entity.blogEntries.BlogEntry;

import jakarta.validation.Valid;

public interface BlogEntryService {
    public ResponseEntity<BlogTitlesResponse> getBlogTitlesPage(Pageable pageable);

    public ResponseEntity<BlogEntry> getBlogEntryById( @PathVariable Long id);

    public ResponseEntity<BlogEntry> findNewestBlogEntry();

    public ResponseEntity<BlogEntry> createBlogEntry(@Valid @RequestBody BlogEntry blogEntry);

    public ResponseEntity<BlogEntry> getBlogEntry();
}
