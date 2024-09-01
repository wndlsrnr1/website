package com.website.controller.admin.item.model;

import com.website.controller.api.common.model.ApiResponse;
import com.website.repository.model.category.Subcategory;
import com.website.service.admin.item.model.ItemResponseDto;
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
