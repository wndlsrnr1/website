package com.website.web.controller.api.admin.item;

import com.website.web.dto.request.item.SaveItemRequest;
import com.website.web.dto.sqlcond.item.ItemSearchCond;
import com.website.web.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add")
    public ResponseEntity sendResultOfSaveItemDetailsByItemFormRequest(@Validated SaveItemRequest saveItemRequest, BindingResult bindingResult) {
        return itemService.saveItemByItemFormRequest(saveItemRequest, bindingResult);
    }

}
