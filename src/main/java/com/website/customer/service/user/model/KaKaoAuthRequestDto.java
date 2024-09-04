package com.website.customer.service.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KaKaoAuthRequestDto {
    private String code;
    private String error;
    private String errorDescription;
    private String state;
}
