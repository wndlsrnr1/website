package com.website.customer.controller.thumbnail;

import com.website.common.controller.model.ApiResponse;
import com.website.customer.controller.thumbnail.model.ThumbnailResponse;
import com.website.customer.service.thumbnail.CustomerThumbnailService;
import com.website.customer.service.thumbnail.model.ThumbnailResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CustomerThumbnailController {

    private final CustomerThumbnailService thumbnailService;

    @GetMapping("/items/{itemId}/attachments/thumbnail")
    public ApiResponse<ThumbnailResponse> getThumbnailImageByItemId(
            @PathVariable(value = "itemId") Long itemId
    ) {
        ThumbnailResponseDto dto = thumbnailService.findByItem(itemId);
        return ApiResponse.success(ThumbnailResponse.of(dto));
    }

}
