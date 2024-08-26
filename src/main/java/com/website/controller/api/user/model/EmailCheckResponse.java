package com.website.controller.api.user.model;

import com.website.service.user.model.EmailCheckResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailCheckResponse {

    private boolean emailExists;

    public static EmailCheckResponse of(EmailCheckResponseDto dto) {
        return EmailCheckResponse.builder().emailExists(dto.isEmailExists()).build();
    }
}
