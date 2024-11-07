package com.michael_gregory.blog_api.rest;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.michael_gregory.blog_api.dao.BlogEntryRepository;
import com.michael_gregory.blog_api.dto.BlogTitleDTO;
import com.michael_gregory.blog_api.dto.BlogTitlesResponse;
import com.michael_gregory.blog_api.entity.BlogEntry;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api")
public class BlogEntryController {

    @Autowired
    private BlogEntryRepository blogEntryRepository;


    // Ex: GET http://localhost:8080/api/findAllTitles?page=0&size20&sort=title,asc
    @GetMapping("/public/find-all-titles")
    public ResponseEntity<BlogTitlesResponse> findAllTitles(Pageable pageable) {
        Page<BlogTitleDTO> page = blogEntryRepository.findAllTitles(pageable);
        int pageCount = page.getTotalPages();
        List<BlogTitleDTO> blogTitles = page.getContent();
        BlogTitlesResponse response = new BlogTitlesResponse(blogTitles, pageCount);
        if(!blogTitles.isEmpty()){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/public/blog-entries/{id}")
    public ResponseEntity<BlogEntry> findBlogEntryById( @PathVariable Long id) {
        Optional<BlogEntry> blogEntry = blogEntryRepository.findById(id);
        return blogEntry.map(ResponseEntity::ok)
        .orElseGet(()->ResponseEntity.notFound().build());
    }

    // Find the newest blog entry.
    // Uses Spring Data REST pagination to fetch only the required record.
    @GetMapping("/public/latest-blog-entry")
    public ResponseEntity<BlogEntry> findNewestBlogEntry() {
        return blogEntryRepository.findAll(PageRequest.of(0, 1, 
        Sort.by(Sort.Direction.DESC, "createdAt")))
        .getContent()
        .stream()
        .findFirst().map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/blog-entries")
    public ResponseEntity<BlogEntry> createBlogEntry(@Valid @RequestBody BlogEntry blogEntry) {
        BlogEntry savedEntry = blogEntryRepository.save(blogEntry);
        return new ResponseEntity<>(savedEntry, HttpStatus.CREATED);
    }

    //test endpoint. returns the newest blog entry for admin
    @GetMapping("/blog-entries")
    public ResponseEntity<BlogEntry> getBlogEntry() {
        return blogEntryRepository.findAll(PageRequest.of(0, 1, 
        Sort.by(Sort.Direction.DESC, "createdAt")))
        .getContent()
        .stream()
        .findFirst().map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());    }
}
