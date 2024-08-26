package com.website.controller.api.item;

import com.website.controller.api.common.model.ApiResponse;
import com.website.controller.api.common.model.PageResultResponse;
import com.website.controller.api.item.model.ItemWithReviewResponse;
import com.website.controller.api.item.model.SearchItemResponse;
import com.website.repository.item.model.ItemSearchSortType;
import com.website.repository.item.model.ItemWithReviewSortType;
import com.website.service.common.model.PageResultDto;
import com.website.service.item.ItemService;
import com.website.service.item.model.ItemWithReviewDto;
import com.website.service.item.model.ItemWithReviewSearchRequestDto;
import com.website.service.item.model.SearchItemDto;
import com.website.service.item.model.SearchItemRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.stream.Collectors;

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
    @GetMapping("/comments")
    public ResponseEntity responseCommentResponse(@RequestParam(value = "itemId", required = false) Long itemId) {
        return itemService.getCommentResponse(itemId);
    }

    @GetMapping
    public ApiResponse<PageResultResponse<SearchItemResponse>> search(
            @RequestParam @Min(1) @Max(100) Integer size,
            @RequestParam ItemSearchSortType sortType,
            @RequestParam(required = false) String searchAfter,
            @RequestParam(required = false, defaultValue = "false") boolean withTotalCount,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long subcategoryId,
            @RequestParam(required = false) String searchName,
            @RequestParam(required = false, defaultValue = "false") boolean onDiscount
    ) {
        // Make dto
        SearchItemRequestDto dto = SearchItemRequestDto.builder()
                .size(size)
                .sortType(sortType)
                .searchAfter(searchAfter)
                .withTotalCount(withTotalCount)
                .categoryId(categoryId)
                .subcategoryId(subcategoryId)
                .searchName(searchName)
                .onDiscount(onDiscount)
                .build();

        // Search
        PageResultDto<SearchItemDto> resultDto = itemService.search(dto);

        // Make Response
        PageResultResponse<SearchItemResponse> pageResult = PageResultResponse.<SearchItemResponse>builder()
                .items(resultDto.getItems().stream().map(SearchItemResponse::of).collect(Collectors.toList()))
                .nextSearchAfter(resultDto.getNextSearchAfter())
                .totalCount(resultDto.getTotalCount())
                .build();

        return ApiResponse.success(pageResult);
    }

    @GetMapping("/reviewed")
    public ApiResponse<PageResultResponse<ItemWithReviewResponse>> searchReviews(
            @RequestParam @Min(1) Integer size,
            @RequestParam(required = false) String nextSearchAfter,
            @RequestParam(required = false, defaultValue = "false") boolean withTotalCount,
            @RequestParam(required = false, defaultValue = "RECENT") ItemWithReviewSortType sortType
    ) {
        ItemWithReviewSearchRequestDto dto = ItemWithReviewSearchRequestDto.builder()
                .nextSearchAfter(nextSearchAfter)
                .size(size)
                .sortType(sortType)
                .withTotalCount(withTotalCount)
                .build();

        PageResultDto<ItemWithReviewDto> resultDto = itemService.searchItemWithReview(dto);
        PageResultResponse<ItemWithReviewResponse> resultResponse = PageResultResponse.<ItemWithReviewResponse>builder()
                .items(resultDto.getItems().stream().map(ItemWithReviewResponse::of).collect(Collectors.toList()))
                .nextSearchAfter(resultDto.getNextSearchAfter())
                .totalCount(resultDto.getTotalCount())
                .build();

        return ApiResponse.success(resultResponse);
    }

}
