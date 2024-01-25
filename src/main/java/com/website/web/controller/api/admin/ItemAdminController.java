package com.website.web.controller.api.admin;

import com.website.web.dto.sqlcond.item.ItemSearchCond;
import com.website.web.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/items")
public class ItemAdminController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity sendItemDtoBySearchCond(@Validated ItemSearchCond itemSearchCond, BindingResult bindingResult, Pageable pageable) {
        log.info("itemSearchCond = {}", itemSearchCond);
        return itemService.sendItemResponseByCond(itemSearchCond, bindingResult, pageable);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity sendItemDetails(@PathVariable Long itemId) {
        return itemService.sendItemDetailPageByItemId(itemId);
    }

}
