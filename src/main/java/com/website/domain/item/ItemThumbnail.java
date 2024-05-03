package com.website.domain.item;

import com.website.domain.attachment.Attachment;
import com.website.domain.common.AbstractBaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ItemThumbnail {
    @Id
    @Column(name = "item_thumbnail_id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachment_id", nullable = false)
    private Attachment attachment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId", nullable = false)
    private Item item;

    public ItemThumbnail(Attachment attachment, Item item) {
        this.attachment = attachment;
        this.item = item;
    }
}
