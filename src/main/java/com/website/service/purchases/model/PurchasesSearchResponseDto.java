package com.website.service.purchases.model;


import com.website.repository.purchases.model.OrderStatus;
import com.website.repository.purchases.model.PurchasesSearch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchasesSearchResponseDto {

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

    public static PurchasesSearchResponseDto of(PurchasesSearch purchasesSearch) {
        return PurchasesSearchResponseDto.builder()
                .id(purchasesSearch.getId())
                .userId(purchasesSearch.getUserId())
                .email(purchasesSearch.getEmail())
                .reviewId(purchasesSearch.getReviewId())
                .orderNumber(purchasesSearch.getOrderNumber())
                .orderDate(purchasesSearch.getOrderDate())
                .itemId(purchasesSearch.getItemId())
                .itemNameKor(purchasesSearch.getItemNameKor())
                .imageIdForThumbnail(purchasesSearch.getImageIdForThumbnail())
                .status(purchasesSearch.getStatus())
                .totalAmount(purchasesSearch.getTotalAmount())
                .address(purchasesSearch.getAddress())
                .discount(purchasesSearch.getDiscount())
                .createdAt(purchasesSearch.getCreatedAt())
                .updatedAt(purchasesSearch.getUpdatedAt())
                .build();
    }
}
