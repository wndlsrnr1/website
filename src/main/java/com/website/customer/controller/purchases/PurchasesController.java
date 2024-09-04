package com.website.customer.controller.purchases;

import com.website.config.auth.AdminUser;
import com.website.config.auth.LoginUser;
import com.website.config.auth.ServiceUser;
import com.website.common.controller.model.ApiResponse;
import com.website.common.controller.model.PageResultResponse;
import com.website.customer.controller.purchases.model.CreatePurchasesRequest;
import com.website.customer.controller.purchases.model.PurchasesResponse;
import com.website.customer.controller.purchases.model.PurchasesSearchResponse;
import com.website.common.repository.purchases.model.OrderStatus;
import com.website.common.repository.purchases.model.PurchasesSortType;
import com.website.common.service.model.PageResultDto;
import com.website.customer.service.purchases.PurchasesService;
import com.website.customer.service.purchases.model.CreatePurchasesRequestDto;
import com.website.customer.service.purchases.model.PurchasesResponseDto;
import com.website.customer.service.purchases.model.PurchasesSearchRequestDto;
import com.website.customer.service.purchases.model.PurchasesSearchResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @LoginUser
    @PostMapping("/purchases")
    public ApiResponse<PurchasesResponse> createPurchase(
            @AuthenticationPrincipal ServiceUser serviceUser,
            @Valid @RequestBody CreatePurchasesRequest request
    ) {
        CreatePurchasesRequestDto dto = request.toDto(serviceUser.getId());
        PurchasesResponseDto responseDto = purchasesService.create(dto);
        return ApiResponse.success(PurchasesResponse.of(responseDto));
    }


}
