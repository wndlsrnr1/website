package com.website.domain.item;

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

    private Integer seq;
}
