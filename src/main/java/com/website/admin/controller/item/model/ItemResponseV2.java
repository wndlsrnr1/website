package com.website.admin.controller.item.model;

import com.website.admin.service.model.ItemResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ItemResponseV2 {
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

    public static ItemResponseV2 of(ItemResponseDto dto) {
        return ItemResponseV2.builder()
                .id(dto.getId())
                .name(dto.toString())
                .nameKor(dto.getNameKor())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .status(dto.getStatus())
                .description(dto.getDescription())
                .releasedAt(dto.getReleasedAt())
                .updatedAt(dto.getUpdatedAt())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}
