package com.website.repository.purchases.model;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PurchasesSearch {
    private Long id;
    private Long userId;
    private String email;
    private Long reviewId;
    private String orderNumber;
    private LocalDateTime orderDate;
    private Long itemId;
    private String itemNameKor;
    private Long imageIdForThumbnail;
    private OrderStatus status;
    private Integer totalAmount;
    private String address;
    private Integer discount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @QueryProjection
    public PurchasesSearch(Long id, Long userId, String email, Long reviewId, String orderNumber, LocalDateTime orderDate, Long itemId, String itemNameKor, Long imageIdForThumbnail, OrderStatus status, Integer totalAmount, String address, Integer discount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.reviewId = reviewId;
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.itemId = itemId;
        this.itemNameKor = itemNameKor;
        this.imageIdForThumbnail = imageIdForThumbnail;
        this.status = status;
        this.totalAmount = totalAmount;
        this.address = address;
        this.discount = discount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
