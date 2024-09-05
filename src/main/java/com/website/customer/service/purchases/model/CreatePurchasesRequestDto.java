package com.website.customer.service.purchases.model;

import com.website.common.repository.model.item.Item;
import com.website.common.repository.purchases.model.Purchases;
import com.website.common.repository.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreatePurchasesRequestDto {
    private Long itemId;

    private Long userId;

    private Integer totalAmount;

    private String address;

    public Purchases toEntity(User user, Item item) {
        return Purchases.builder()
                .item(item)
                .user(user)
                .totalAmount(totalAmount)
                .address(address)
                .orderNumber("order: " + UUID.randomUUID())
                .build();
    }

}
