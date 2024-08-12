package com.website.service.review.model;

import com.website.repository.model.item.Item;
import com.website.repository.model.user.User;
import com.website.repository.review.model.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewCreateDto {
    private Item item;
    private User user;
    private Integer star;
    private String content;

    public Review toEntity() {
        return Review.builder()
                .item(item)
                .user(user)
                .star(star)
                .content(content)
                .build();
    }
}
