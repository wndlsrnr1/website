package com.website.controller.api.purchases;

import com.website.config.auth.LoginUser;
import com.website.config.auth.ServiceUser;
import com.website.controller.api.common.model.ApiResponse;
import com.website.controller.api.common.model.PageResultResponse;
import com.website.controller.api.purchases.model.PurchasesSearchResponse;
import com.website.repository.purchases.model.OrderStatus;
import com.website.repository.purchases.model.PurchasesSortType;
import com.website.service.common.model.PageResultDto;
import com.website.service.purchases.PurchasesService;
import com.website.service.purchases.model.PurchasesSearchRequestDto;
import com.website.service.purchases.model.PurchasesSearchResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class PurchasesController {

    private final PurchasesService purchasesService;

    @LoginUser
    @GetMapping("/purchases")
    public ApiResponse<PageResultResponse<PurchasesSearchResponse>> search(
            @AuthenticationPrincipal ServiceUser serviceUser,
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false) OrderStatus orderStatus,
            @RequestParam(required = false, defaultValue = "RECENT") PurchasesSortType sortType,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            @RequestParam(required = false) String nextSearchAfter,
            @RequestParam(required = false, defaultValue = "false") boolean withTotalCount
    ) {
        PurchasesSearchRequestDto dto = PurchasesSearchRequestDto.builder()
                .userId(serviceUser.getId())
                .itemId(itemId)
                .orderStatus(orderStatus)
                .sortType(sortType)
                .size(size)
                .searchAfter(nextSearchAfter)
                .withTotalCount(withTotalCount)
                .build();

        PageResultDto<PurchasesSearchResponseDto> pageResultDto =  purchasesService.search(dto);

        PageResultResponse<PurchasesSearchResponse> pageResultResponse = PageResultResponse.<PurchasesSearchResponse>builder()
                .items(pageResultDto.getItems().stream().map(PurchasesSearchResponse::of).collect(Collectors.toList()))
                .nextSearchAfter(pageResultDto.getNextSearchAfter())
                .totalCount(pageResultDto.getTotalCount())
                .build();

        return ApiResponse.success(pageResultResponse);
    }
}
