package com.website.domain.item;

import com.website.domain.category.Subcategory;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @Column(name = "item_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nameKor;
    private Integer price;
    private Integer quantity;
    private String status;
    private String description;
    private LocalDateTime releaseDate;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)

    @JoinColumn(name = "subcategory_id", nullable = false)
    private Subcategory subcategory;

    @Builder
    public Item(String name, String nameKor, Integer price, Integer quantity, String status, String description, LocalDateTime releaseDate, Subcategory subcategory) {
        this.name = name;
        this.nameKor = nameKor;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.description = description;
        this.releaseDate = releaseDate;
        this.subcategory = subcategory;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNameKor(String nameKor) {
        this.nameKor = nameKor;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }
}
