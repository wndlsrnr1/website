package com.website.web.dto.request.item.carousel;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotNull;

@Data
public class CarouselAddRequest {

    @NotNull
    private Long itemId;
    @NotNull
    private Long attachmentId;
    private Integer priority;

    @QueryProjection
    public CarouselAddRequest(Long itemId, Long attachmentId, Integer priority) {
        this.itemId = itemId;
        this.attachmentId = attachmentId;
        this.priority = priority;
    }

}
