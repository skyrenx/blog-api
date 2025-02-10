package com.michael_gregory.blog_api.dao.blogEntries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.michael_gregory.blog_api.entity.blogEntries.BlogEntrySequence;

import jakarta.persistence.LockModeType;

public interface BlogEntrySequenceRepository extends JpaRepository<BlogEntrySequence, Integer> {

    // Use a pessimistic write lock to ensure only one transaction updates the sequence at a time.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM BlogEntrySequence s")
    BlogEntrySequence findForUpdate();
}
