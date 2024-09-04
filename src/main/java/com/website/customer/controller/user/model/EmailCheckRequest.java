package com.website.customer.controller.user.model;

import com.website.customer.service.user.model.EmailCheckRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

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
