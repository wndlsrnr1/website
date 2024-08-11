package com.website.repository.model.item.review;

import com.website.repository.model.common.AbstractBaseEntity;
import com.website.repository.model.item.Item;
import com.website.repository.model.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends AbstractBaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "review_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //@Max(value = 5)
    //@Min(value = 1)
    private Integer star;

    //private String title;

    private String content;

}
