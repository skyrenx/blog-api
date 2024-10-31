package com.michael_gregory.blog_api.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.michael_gregory.blog_api.entity.BlogEntry;
import com.michael_gregory.blog_api.dto.BlogTitleDTO;

// TODO: Consider moving this repository to the service layer (import into a BlogEntryService),
// since we do not want to expose any of it's endpoits direcly to controllers
@RepositoryRestResource(exported = false)
public interface BlogEntryRepository extends JpaRepository<BlogEntry, Long> {

    // This is an example of JPQL in which queries are written with 
    // java entities and DTOs instead of database tables and columns)
    @Query("SELECT new com.michael_gregory.blog_api.dto.BlogTitleDTO(b.id, b.title) FROM BlogEntry b")
    Page<BlogTitleDTO> findAllTitles(Pageable pageable);

    //@Query("SELECT new com.michael_gregory.blog_api.entity.BlogEntry FROM BlogEntry b WHERE b.c")
    //BlogEntry findNewestBlogEntry(Long id);
}
