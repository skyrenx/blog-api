package com.michael_gregory.blog_api.dao.blogEntries;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.michael_gregory.blog_api.dto.BlogTitle;
import com.michael_gregory.blog_api.entity.blogEntries.BlogEntry;

public interface BlogEntryRepository extends JpaRepository<BlogEntry, Long> {

    // This is an example of JPQL in which queries are written with 
    // java entities and DTOs instead of database tables and columns)
    @Query("SELECT new com.michael_gregory.blog_api.dto.BlogTitle(" +
        "b.id, b.title, b.author, b.createdAt, b.updatedAt) FROM BlogEntry b")
    Page<BlogTitle> findAllTitles(Pageable pageable);

}
