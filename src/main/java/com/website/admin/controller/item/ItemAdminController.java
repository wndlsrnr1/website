package com.website.admin.controller.item;

import com.website.admin.controller.item.model.ItemResponseV2;
import com.website.config.auth.AdminUser;
import com.website.common.controller.model.ApiResponse;
import com.website.admin.service.model.ItemResponseDto;
import com.website.common.service.item.ItemService;
import com.website.common.controller.item.model.EditItemRequest;
import com.website.common.controller.item.model.EditItemRequestV2;
import com.website.common.controller.item.model.SaveItemRequest;
import com.website.common.controller.item.model.ItemSearchCond;
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
    @AdminUser
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

    @AdminUser
    @GetMapping("/{itemId}")
    public ResponseEntity sendItemDetails(
            @PathVariable Long itemId,
            @RequestParam(value = "images", required = false) Boolean images,
            @RequestParam(value = "subcategories", required = false) Boolean subcategories
    ) {
        return itemService.sendItemDetailPageByItemId(itemId);
    }

    @AdminUser
    @GetMapping("/v2/{itemId}")
    public ApiResponse<ItemResponseV2> getItem(
            @PathVariable(value = "itemId") Long itemId
    ) {

        ItemResponseDto responseDto = itemService.getItem(itemId);

        return ApiResponse.success(ItemResponseV2.of(responseDto));
    }

    @AdminUser
    @PostMapping("/add")
    public ResponseEntity sendResultOfSaveItemDetailsByItemFormRequest(@Validated SaveItemRequest saveItemRequest, BindingResult bindingResult) {
        log.info("saveItemRequest = {}", saveItemRequest);
        return itemService.saveItemByItemFormRequest(saveItemRequest, bindingResult);
    }

    @AdminUser
    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity removeItemRequestByItemId(@PathVariable Long itemId) {
        return itemService.removeItemByItemId(itemId);
    }

    @AdminUser
    @DeleteMapping("/image/remove")
    public ResponseEntity removeAttachmentOnItemByAttachmentId(List<Long> fileIdList) {
        return itemService.deleteFileOnItem(fileIdList);
    }

    //@PostMapping("/edit/{itemId}")
    public ResponseEntity editItemRequest(@PathVariable(name = "itemId", required = true) Long itemId, @Validated EditItemRequest editItemRequest, BindingResult bindingResult) {
        return itemService.editItemFormOnAdmin(itemId, editItemRequest, bindingResult);
    }

    @AdminUser
    @PostMapping("/edit/{itemId}")
    public ResponseEntity editItemRequestV2(@PathVariable(name = "itemId", required = true) Long itemId, EditItemRequestV2 editItemRequest, BindingResult bindingResult) {
        return itemService.editItemFormOnAdminV2(itemId, editItemRequest, bindingResult);
    }

    @AdminUser
    @GetMapping("/thumbnail/{itemId}")
    public ResponseEntity sendThumbnailResponse(@PathVariable("itemId") Long itemId) {
        return itemService.getThumbnailResponse(itemId);
    }

    @AdminUser
    @PostMapping("/thumbnail/edit/{itemId}")
    public ResponseEntity editThumbnailResponse(@PathVariable("itemId") Long itemId, @RequestParam("imageId") Long imageId) {
        //썸네일이 이미 없는 경우 insert하기
        return itemService.editThumbnail(itemId, imageId);
    }

    @AdminUser
    @PostMapping("/thumbnail/add/{itemId}")
    public ResponseEntity addThumbnailResponse(@PathVariable("itemId") Long itemId, @RequestParam("imageId") Long imageId) {
        return itemService.addThumbnail(itemId, imageId);
    }

    //        /admin/items/info?itemId=" + itemId
    @AdminUser
    @GetMapping("/info")
    public ResponseEntity responseItemInfo(@RequestParam(value = "itemId", required = false) Long itemId) {
        log.info("itemid = {}", itemId);
        return itemService.getResponseItemInfo(itemId);
    }

    @AdminUser
    @GetMapping("/sequence/{itemId}")
    public ResponseEntity responseAttachmentSequence(@PathVariable("itemId") Long itemId) {
        return itemService.getItemAttachmentSequence(itemId);
    }

}
