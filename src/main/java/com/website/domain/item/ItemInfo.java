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

    private String brand;

    private String manufacturer;

    @Column(name = "made_in")
    private String madeIn;


    public ItemInfo(Item item, Long views, Integer salesRate) {
        this.item = item;
        this.views = views;
        this.salesRate = salesRate;
    }

    public ItemInfo(Long id, Item item, Long views, Integer salesRate, String brand, String manufacturer, String madeIn) {
        Id = id;
        this.item = item;
        this.views = views;
        this.salesRate = salesRate;
        this.brand = brand;
        this.manufacturer = manufacturer;
        this.madeIn = madeIn;
    }
}
