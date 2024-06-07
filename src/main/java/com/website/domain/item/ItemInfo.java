package com.website.domain.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemInfo {

    @Id
    @GeneratedValue
    @Column(name = "item_info_id", nullable = false)
    private Long Id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private Long views;
    private Integer salesRate;

    public ItemInfo(Item item, Long views, Integer salesRate) {
        this.item = item;
        this.views = views;
        this.salesRate = salesRate;
    }
}
