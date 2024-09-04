package com.website.customer.service.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoUserAudInfoDto {
    @JsonProperty("target_id_type")
    private String targetIdType;
    @JsonProperty("target_id")
    private Long targetId;
}
