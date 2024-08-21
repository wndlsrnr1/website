package com.website.controller.api.review.model;

import com.website.service.review.model.ReviewUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUpdateRequest {
    @Min(0)
    @Max(5)
    @NotNull
    private Integer star;

    @NotBlank
    private String content;

    public ReviewUpdateDto toDto(Long userId, Long itemId) {
        return ReviewUpdateDto.builder()
                .userId(userId)
                .star(star)
                .content(content)
                .itemId(itemId)
                .build();
    }
}
