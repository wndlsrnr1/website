package com.website.common.repository.bookmark.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "item_id"})})
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "item_id")
    private Long itemId;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public static Bookmark create(Long userId, Long itemId) {
        return Bookmark.builder()
                .userId(userId)
                .itemId(itemId)
                .build();
    }
}
