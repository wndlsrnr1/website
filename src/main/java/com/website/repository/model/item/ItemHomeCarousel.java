package com.website.repository.model.item;

import com.website.repository.model.common.AbstractBaseEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class ItemHomeCarousel extends AbstractBaseEntity {
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

    public ItemHomeCarousel(Long itemId, Long attachmentId) {
        this.itemId = itemId;
        this.attachmentId = attachmentId;
    }

    public ItemHomeCarousel() {

    }

}
