package com.website.web.controller.api.item;

import com.website.web.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
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
}
