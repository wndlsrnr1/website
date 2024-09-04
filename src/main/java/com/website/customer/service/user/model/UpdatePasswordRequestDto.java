package com.website.customer.service.user.model;

import com.website.utils.common.constance.Regexes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdatePasswordRequestDto {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

}
