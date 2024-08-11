package com.website.controller.api.model.request.item.carousel;

import lombok.Data;

@Data
public class CarouselUpdateRequest {
    private Long id;
    private Long itemId;
    private Long attachmentId;
    private Integer priority;
}
