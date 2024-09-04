package com.website.customer.controller.user.model;

import com.website.customer.service.user.model.UpdatePasswordRequestDto;
import com.website.utils.common.constance.Regexes;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UpdatePasswordRequest {
    
    @NotBlank
    private String currentPassword;
    @NotBlank
    @Pattern(regexp = Regexes.PASSWORD_PATTERN)
    private String newPassword;
    @NotBlank
    private String confirmPassword;

    public UpdatePasswordRequestDto toDto() {
        return UpdatePasswordRequestDto.builder()
                .currentPassword(currentPassword)
                .newPassword(newPassword)
                .confirmPassword(confirmPassword)
                .build();
    }
}
