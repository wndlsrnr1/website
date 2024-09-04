package com.website.common.repository.model.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "item_attachment_seq")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemAttachmentSeq {

    @Id
    @GeneratedValue
    @Column(name = "item_attachment_seq_id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_attachment_id")
    private ItemAttachment itemAttachment;

    private Integer seq;


    public ItemAttachmentSeq(ItemAttachment itemAttachment, Integer seq) {
        this.itemAttachment = itemAttachment;
        this.seq = seq;
    }
}
