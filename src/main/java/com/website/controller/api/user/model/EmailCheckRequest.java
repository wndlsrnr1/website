package com.website.controller.api.user.model;

import com.website.service.user.model.EmailCheckRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailCheckRequest {

    @NotEmpty
    @Email
    private String email;


    public EmailCheckRequestDto toDto() {
        return EmailCheckRequestDto.builder().email(email).build();
    }
}
