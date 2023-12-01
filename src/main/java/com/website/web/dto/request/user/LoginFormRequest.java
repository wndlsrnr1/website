package com.website.web.dto.request.user;

import com.website.web.commonconstance.Regexes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginFormRequest {
    @NotBlank
    @Email
    private String email;

    //@Pattern(regexp = Regexes.PASSWORD_PATTERN, message = "${Pattern.password}")
    private String password;
}
