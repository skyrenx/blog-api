package com.michael_gregory.blog_api.service;

import com.michael_gregory.blog_api.dao.blogEntries.BlogEntrySequenceRepository;
import com.michael_gregory.blog_api.entity.blogEntries.BlogEntrySequence;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SequenceServiceImpl implements SequenceService {

    private final BlogEntrySequenceRepository blogEntrySequenceRepository;

    public SequenceServiceImpl(BlogEntrySequenceRepository blogEntrySequenceRepository) {
        this.blogEntrySequenceRepository = blogEntrySequenceRepository;
    }

    @Override
    @Transactional
    public int getNextBlogEntryId() {
        // Retrieve the sequence row with a PESSIMISTIC_WRITE lock.
        BlogEntrySequence sequence = blogEntrySequenceRepository.findForUpdate();
        if (sequence == null) {
            throw new EntityNotFoundException("No entry found in table blog_entry_sequence");
        }
        int currentNextId = sequence.getNextId();
        // Increment the sequence.
        sequence.setNextId(currentNextId + 1);
        blogEntrySequenceRepository.save(sequence);
        return currentNextId;
    }
}
