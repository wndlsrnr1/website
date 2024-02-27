package com.website.domain.item;

import javax.persistence.*;

@Entity
public class ItemHomeCarousel {
    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn(name = "item_id", nullable = false)
    private Long itemId;

    @JoinColumn(name = "attachment_id", nullable = false)
    private Long attachmentId;

    private Integer priority;

    public ItemHomeCarousel(Long itemId, Long attachmentId, Integer priority) {
        this.itemId = itemId;
        this.attachmentId = attachmentId;
        this.priority = priority;
    }

    public ItemHomeCarousel() {
    }
}
