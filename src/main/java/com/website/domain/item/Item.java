package com.website.domain.item;

import com.website.domain.category.Subcategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.NONE)
public class Item {
    @Id
    @Column(name = "item_id", nullable = false)
    @GeneratedValue
    private Long id;
    private String name;
    private String nameKor;
    private Integer price;
    private Integer quantity;
    private String status;
    private String description;
    private Date releaseDate;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "subcategory_id", nullable = false)
    private Subcategory subcategory;

}
