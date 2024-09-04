package com.website.customer.controller.purchases.model;

import com.website.common.repository.purchases.model.OrderStatus;
import com.website.customer.service.purchases.model.PurchasesResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PurchasesResponse {
    private String orderNumber;
    private LocalDateTime orderDate;
    //private User user;
    private String email;
    private String username;
    //private Item item;
    private Long itemId;
    private String itemNameKor;
    private Integer discount;

    private OrderStatus status;
    private Integer totalAmount;
    private String address;
    private LocalDateTime createdAt;

    public static PurchasesResponse of(PurchasesResponseDto dto) {
        return PurchasesResponse.builder()
                .orderNumber(dto.getOrderNumber())
                .orderDate(dto.getOrderDate())
                .email(dto.getEmail())
                .username(dto.getUsername())
                .itemId(dto.getItemId())
                .itemNameKor(dto.getItemNameKor())
                .discount(dto.getDiscount())
                .status(dto.getStatus())
                .totalAmount(dto.getTotalAmount())
                .address(dto.getAddress())
                .createdAt(dto.getCreatedAt())
                .build();
    }

}
