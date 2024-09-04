package com.website.common.repository.item.carousel.model;

import lombok.Data;

@Data
public class CarouselUpdateRequest {
    private Long id;
    private Long itemId;
    private Long attachmentId;
    private Integer priority;
}
