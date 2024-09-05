package com.website.customer.controller.purchases.model;

import com.website.customer.service.purchases.model.CreatePurchasesRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreatePurchasesRequest {
    @Min(value = 1)
    @Max(value = 10)
    private Integer totalAmount;

    @NotBlank
    @Length(min = 8, max = 100)
    private String address;

    public CreatePurchasesRequestDto toDto(Long userId, Long itemId) {
        return CreatePurchasesRequestDto.builder()
                .userId(userId)
                .itemId(itemId)
                .totalAmount(totalAmount)
                .address(address)
                .build();
    }

}
