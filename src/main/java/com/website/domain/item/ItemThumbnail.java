package com.website.domain.item;

import com.website.domain.attachment.Attachment;
import com.website.domain.common.AbstractBaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemThumbnail extends AbstractBaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "item_thumbnail_id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachment_id")
    private Attachment attachment;

    public ItemThumbnail(Item item, Attachment attachment) {
        this.item = item;
        this.attachment = attachment;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }
}
