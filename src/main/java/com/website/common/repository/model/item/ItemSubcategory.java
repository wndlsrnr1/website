package com.website.common.repository.model.item;

import com.website.common.repository.model.category.Subcategory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemSubcategory {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;

    public ItemSubcategory(Item item, Subcategory subcategory) {
        this.item = item;
        this.subcategory = subcategory;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }
}
