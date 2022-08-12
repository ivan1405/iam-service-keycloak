package com.chapter.backend.services.iamservice.crud.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
public class BaseEntity {

    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        creationDate = now;
        lastModified = now;
    }

    @PreUpdate
    public void onUpdate() {
        lastModified = LocalDateTime.now();
    }
}