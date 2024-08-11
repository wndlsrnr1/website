package com.website.controller.api.model.request.item.carousel;

import lombok.Data;

@Data
public class CarouselPriorityUpdateRequest {
    private Long id;
    private int priority;
}
