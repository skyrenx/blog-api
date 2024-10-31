package com.michael_gregory.blog_api.rest;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.michael_gregory.blog_api.dao.BlogEntryRepository;
import com.michael_gregory.blog_api.dto.BlogTitleDTO;
import com.michael_gregory.blog_api.entity.BlogEntry;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api")
public class BlogEntryController {

    @Autowired
    private BlogEntryRepository blogEntryRepository;


    // Ex: GET http://localhost:8080/api/findAllTitles?page=0&size=10&sort=title,asc
    @GetMapping("/find-all-titles")
    public ResponseEntity<List<BlogTitleDTO>> findAllTitles(Pageable pageable) {
        List<BlogTitleDTO> blogTitles = blogEntryRepository.findAllTitles(pageable)
        .getContent();
        if(!blogTitles.isEmpty()){
            return ResponseEntity.ok(blogTitles);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/blog-entries/{id}")
    public ResponseEntity<BlogEntry> findBlogEntryById( @PathVariable Long id) {
        Optional<BlogEntry> blogEntry = blogEntryRepository.findById(id);
        return blogEntry.map(ResponseEntity::ok)
        .orElseGet(()->ResponseEntity.notFound().build());
    }

    // Find the newest blog entry.
    // Uses Spring Data REST pagination to fetch only the required record.
    @GetMapping("/latest-blog-entry")
    public ResponseEntity<BlogEntry> findNewestBlogEntry() {
        return blogEntryRepository.findAll(PageRequest.of(0, 1, 
        Sort.by(Sort.Direction.DESC, "createdAt")))
        .getContent()
        .stream()
        .findFirst().map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
