package com.website.web.dto.request.item.carousel;

import lombok.Data;

@Data
public class CarouselPriorityUpdateRequest {
    private Long id;
    private int priority;
}
