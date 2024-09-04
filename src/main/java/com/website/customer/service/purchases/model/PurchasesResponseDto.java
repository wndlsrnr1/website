package com.website.customer.service.purchases.model;

import com.website.common.repository.purchases.model.OrderStatus;
import com.website.common.repository.purchases.model.Purchases;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchasesResponseDto {
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

    public static PurchasesResponseDto of(Purchases domain) {
        return PurchasesResponseDto.builder()
                .orderNumber(domain.getOrderNumber())
                .orderDate(domain.getOrderDate())
                .email(domain.getUser().getEmail())
                .username(domain.getUser().getName())
                .itemId(domain.getItem().getId())
                .itemNameKor(domain.getItem().getNameKor())
                .discount(domain.getDiscount())
                .status(domain.getStatus())
                .totalAmount(domain.getTotalAmount())
                .address(domain.getAddress())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
