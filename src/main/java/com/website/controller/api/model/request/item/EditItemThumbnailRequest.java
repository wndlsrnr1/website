package com.website.controller.api.model.request.item;

import lombok.Data;

@Data
public class EditItemThumbnailRequest {
    private Long ItemId;
    private Long imageId;
}
