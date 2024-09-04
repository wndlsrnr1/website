package com.website.customer.controller.user.model;

import com.website.customer.service.user.model.KaKaoAuthRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KaKaoAuthRequest {

    @NotBlank
    private String code;
    private String error;
    private String errorDescription;
    private String state;


    public KaKaoAuthRequestDto toDto() {
        return KaKaoAuthRequestDto.builder()
                .code(code)
                .error(error)
                .errorDescription(errorDescription)
                .state(state)
                .build();
    }
}
