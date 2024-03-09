package com.website.web.dto.request.item.carousel;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Data
public class CarouselUpdateRequest {
    private Long id;
    private Long itemId;
    private Long attachmentId;
    private Integer priority;
}
