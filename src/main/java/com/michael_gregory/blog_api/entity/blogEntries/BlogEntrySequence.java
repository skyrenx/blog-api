package com.michael_gregory.blog_api.entity.blogEntries;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "blog_entry_sequence")
public class BlogEntrySequence {

    @Id
    @Column(name = "next_id")
    private Integer nextId;

    public Integer getNextId() {
        return nextId;
    }

    public void setNextId(Integer nextId) {
        this.nextId = nextId;
    }
}
