package com.michael_gregory.blog_api.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.michael_gregory.blog_api.entity.BlogEntry;

//@RepositoryRestResource(path="entries")
public interface BlogEntryRepository extends JpaRepository<BlogEntry, Long> {

}
