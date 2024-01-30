package com.website.domain.item;

import com.website.domain.attachment.Attachment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "item_attachment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemAttachment {

    @Id
    @GeneratedValue
    @Column(name = "item_attachment_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachment_id", nullable = false)
    private Attachment attachment;


    public ItemAttachment(Item item, Attachment attachment) {
        this.item = item;
        this.attachment = attachment;
    }
}
