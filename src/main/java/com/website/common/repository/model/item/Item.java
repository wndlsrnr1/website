package com.website.common.repository.model.item;

import com.website.common.repository.model.common.AbstractBaseEntity;
import com.website.common.repository.user.model.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
//@EntityListeners(ItemListener.class)
public class Item extends AbstractBaseEntity {

    @Id
    @Column(name = "item_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String name;
    private String nameKor;
    private Integer price;
    private Integer quantity;
    private String status;
    private String description;
    private LocalDateTime releasedAt;

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

