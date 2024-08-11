package com.website.web.controller.api.item;

import com.website.web.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/thumbnail")
    public ResponseEntity responseThumbnail(@RequestParam(value = "itemId", required = false) Long itemId) {
        return itemService.getThumbnailResponse(itemId);
    }

    @GetMapping("/basic/{itemId}")
    public ResponseEntity responseItemBasic(@PathVariable(value = "itemId", required = false) Long itemId) {
        return itemService.getItemBasicResponse(itemId);
    }
    @GetMapping("/reviews")
    public ResponseEntity responseReviewResponse(@RequestParam(value = "itemId", required = false) Long itemId) {
        return itemService.getReviewResponse(itemId);
    }
}
