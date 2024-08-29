package com.website.controller.api.user.model;

import com.website.service.user.model.UpdateAddressRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAddressRequest {
    @NotBlank
    @Length(min = 8, max = 50)
    private String address;

    public UpdateAddressRequestDto toDto() {
        return UpdateAddressRequestDto.builder()
                .address(address)
                .build();
    }
}
