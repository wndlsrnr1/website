package com.website.domain.common;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Calendar;

@MappedSuperclass
public class AbstractBaseEntity {
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Calendar createdAt;

    @Column(name = "updated_at", nullable = false, updatable = true)
    private Calendar updatedAt;

    @PrePersist
    protected void onCreated() {
        this.createdAt = Calendar.getInstance();
        this.updatedAt = Calendar.getInstance();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Calendar.getInstance();
    }

    @Override
    public String toString() {
        return "AbstractBaseEntity{" +
                "createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
