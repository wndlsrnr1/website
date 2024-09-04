package com.website.admin.service.model;

import com.website.common.repository.model.item.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemResponseDto {
    private Long id;
    private String name;
    private String nameKor;
    private Integer price;
    private Integer quantity;
    private String status;
    private String description;
    private LocalDateTime releasedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    public static ItemResponseDto of(Item item) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .nameKor(item.getNameKor())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .status(item.getStatus())
                .description(item.getDescription())
                .releasedAt(item.getReleasedAt())
                .updatedAt(item.getReleasedAt())
                .createdAt(item.getReleasedAt())
                .build();
    }
}
