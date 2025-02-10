package com.michael_gregory.blog_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.michael_gregory.blog_api.dao.blogEntries.BlogEntryRepository;
import com.michael_gregory.blog_api.dto.BlogTitle;
import com.michael_gregory.blog_api.dto.BlogTitlesResponse;
import com.michael_gregory.blog_api.entity.blogEntries.BlogEntry;

import jakarta.validation.Valid;

@Service
public class BlogEntryServiceImpl implements BlogEntryService{

    private BlogEntryRepository blogEntryRepository;
    private SequenceService sequenceService;

    public BlogEntryServiceImpl(BlogEntryRepository blogEntryRepository, SequenceService sequenceService) {
        this.blogEntryRepository = blogEntryRepository;
    }

    @Override
    public ResponseEntity<BlogTitlesResponse> getBlogTitlesPage(Pageable pageable) {
        Page<BlogTitle> page = blogEntryRepository.findAllTitles(pageable);
        int pageCount = page.getTotalPages();
        List<BlogTitle> blogTitles = page.getContent();
        BlogTitlesResponse response = new BlogTitlesResponse(blogTitles, pageCount);
        if(!blogTitles.isEmpty()){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<BlogEntry> getBlogEntryById( @PathVariable Long id){
        Optional<BlogEntry> blogEntry = blogEntryRepository.findById(id);
        return blogEntry.map(ResponseEntity::ok)
        .orElseGet(()->ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<BlogEntry> findNewestBlogEntry() {
        //ResponseEntity<Blo
        return blogEntryRepository.findAll(PageRequest.of(0, 1, 
        Sort.by(Sort.Direction.DESC, "createdAt")))
        .getContent()
        .stream()
        .findFirst().map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<BlogEntry> createBlogEntry(@Valid BlogEntry blogEntry) {
        BlogEntry savedEntry = blogEntryRepository.save(blogEntry);
        return new ResponseEntity<>(savedEntry, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<BlogEntry> getBlogEntry() {
        return blogEntryRepository.findAll(PageRequest.of(0, 1, 
        Sort.by(Sort.Direction.DESC, "createdAt")))
        .getContent()
        .stream()
        .findFirst().map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());    
    }
    
}
