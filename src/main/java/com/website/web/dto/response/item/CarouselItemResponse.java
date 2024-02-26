package com.website.web.dto.response.item;

import lombok.Data;

@Data
public class CarouselItemResponse {

    private Long itemId;
    private String itemName;
    private String itemNameKor;

    private Long attachmentId;
    private String savedNameOfAttachment;
    private String requestedNameOfAttachment;

}
