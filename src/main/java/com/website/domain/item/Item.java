package com.website.domain.item;

import com.website.domain.common.AbstractBaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Item extends AbstractBaseEntity {
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
    private LocalDateTime releasedAt;

    @Builder
    public Item(String name, String nameKor, Integer price, Integer quantity, String status, String description, LocalDateTime releasedAt) {
        this.name = name;
        this.nameKor = nameKor;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.description = description;
        this.releasedAt = releasedAt;
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

    public void setReleasedAt(LocalDateTime releaseDate) {
        this.releasedAt = releaseDate;
    }

}
