package com.website.customer.controller.review.model;

import com.website.customer.service.review.model.ReviewCreateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewCreateRequest {
    @Max(5)
    @Min(1)
    @NotNull
    private Integer star;
    @NotBlank
    @Length(min = 10, max = 100)
    private String content;
    @NotNull
    private Long purchasesId;

    public ReviewCreateDto toDto() {
        return ReviewCreateDto.builder()
                .star(star)
                .content(content)
                .purchasesId(purchasesId)
                .build();
    }
}
