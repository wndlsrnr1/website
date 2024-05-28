package com.website.web.controller.api.admin.item;

import com.website.web.dto.request.item.EditItemRequest;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/items")
public class ItemAdminController {

    private final ItemService itemService;

    //@GetMapping
    //public ResponseEntity sendItemDtoBySearchCond(@Validated ItemSearchCond itemSearchCond, BindingResult bindingResult, Pageable pageable) {
    //    log.info("itemSearchCond = {}", itemSearchCond);
    //    return itemService.sendItemResponseByCond(itemSearchCond, bindingResult, pageable);
    //}

    @GetMapping
    public ResponseEntity sendItemDtoBySearchCond(
            @Validated ItemSearchCond itemSearchCond, BindingResult bindingResult, Pageable pageable,
            @RequestParam(required = false) Long lastItemId,
            @RequestParam(required = false) Integer lastPageNumber,
            @RequestParam(required = false) Integer pageChunk,
            @RequestParam(required = false, defaultValue = "false") Boolean isLastPage
    ) {
        log.info("itemSearchCond = {}", itemSearchCond);
        return itemService.sendItemResponseByCondByLastItemId(itemSearchCond, bindingResult, pageable, lastItemId, lastPageNumber, pageChunk, isLastPage);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity sendItemDetails(
            @PathVariable Long itemId,
            @RequestParam(value = "images", required = false) Boolean images,
            @RequestParam(value = "subcategories", required = false) Boolean subcategories
    ) {
        return itemService.sendItemDetailPageByItemId(itemId);
    }

    @PostMapping("/add")
    public ResponseEntity sendResultOfSaveItemDetailsByItemFormRequest(@Validated SaveItemRequest saveItemRequest, BindingResult bindingResult) {
        return itemService.saveItemByItemFormRequest(saveItemRequest, bindingResult);
    }

    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity removeItemRequestByItemId(@PathVariable Long itemId) {
        return itemService.removeItemByItemId(itemId);
    }

    @DeleteMapping("/image/remove")
    public ResponseEntity removeAttachmentOnItemByAttachmentId(List<Long> fileIdList) {
        return itemService.deleteFileOnItem(fileIdList);
    }

    @PostMapping("/edit/{itemId}")
    public ResponseEntity editItemRequest(@PathVariable(name = "itemId", required = true) Long itemId, @Validated EditItemRequest editItemRequest, BindingResult bindingResult) {
        return itemService.editItemFormOnAdmin(itemId, editItemRequest, bindingResult);
    }

    @GetMapping("/thumbnail/{itemId}")
    public ResponseEntity sendThumbnailResponse(@PathVariable("itemId") Long itemId) {
        return itemService.sendThumbnailResponse(itemId);
    }

    @PostMapping("/thumbnail/edit/{itemId}")
    public ResponseEntity editThumbnailResponse(@PathVariable("itemId") Long itemId, @RequestParam("imageId") Long imageId) {
        //썸네일이 이미 없는 경우 insert하기
        return itemService.editThumbnail(itemId, imageId);
    }

    @PostMapping("/thumbnail/add/{itemId}")
    public ResponseEntity addThumbnailResponse(@PathVariable("itemId") Long itemId, @RequestParam("imageId") Long imageId) {
        return itemService.addThumbnail(itemId, imageId);
    }

}
