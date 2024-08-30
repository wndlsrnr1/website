package com.website.controller.api.purchases.model;

import com.website.repository.model.item.Item;
import com.website.repository.purchases.model.OrderStatus;
import com.website.service.purchases.model.PurchasesSearchResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PurchasesSearchResponse {

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

    public static PurchasesSearchResponse of(PurchasesSearchResponseDto dto) {
        return PurchasesSearchResponse.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .email(dto.getEmail())
                .reviewId(dto.getReviewId())
                .orderNumber(dto.getOrderNumber())
                .orderDate(dto.getOrderDate())
                .itemId(dto.getItemId())
                .itemNameKor(dto.getItemNameKor())
                .imageIdForThumbnail(dto.getImageIdForThumbnail())
                .status(dto.getStatus())
                .totalAmount(dto.getTotalAmount())
                .address(dto.getAddress())
                .discount(dto.getDiscount())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
