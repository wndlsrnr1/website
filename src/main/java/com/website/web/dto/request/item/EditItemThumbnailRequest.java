package com.website.web.dto.request.item;

import lombok.Data;

@Data
public class EditItemThumbnailRequest {
    private Long ItemId;
    private Long imageId;
}
