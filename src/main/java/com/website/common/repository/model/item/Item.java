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

    @Version
    private Long version;

    private String name;
    private String nameKor;
    private Integer price;
    private Integer quantity;
    private String status;
    private String description;
    private LocalDateTime releasedAt;


}

