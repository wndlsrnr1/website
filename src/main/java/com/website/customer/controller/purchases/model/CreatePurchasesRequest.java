package com.website.customer.controller.purchases.model;

import com.website.customer.service.purchases.model.CreatePurchasesRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreatePurchasesRequest {
    @NotNull
    private Long itemId;

    @Length(min = 1, max = 100)
    private Integer totalAmount;

    @NotBlank
    @Length(min = 8, max = 100)
    private String address;

    public CreatePurchasesRequestDto toDto(Long userId) {
        return CreatePurchasesRequestDto.builder()
                .userId(userId)
                .itemId(itemId)
                .totalAmount(totalAmount)
                .address(address)
                .build();
    }

}
