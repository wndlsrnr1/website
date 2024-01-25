package com.website.web.service.item;

import com.website.domain.item.Item;
import com.website.repository.item.ItemRepository;
import com.website.web.dto.common.ApiError;
import com.website.web.dto.common.ApiResponseBody;
import com.website.web.dto.response.item.ItemResponse;
import com.website.web.dto.sqlcond.item.ItemSearchCond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    //private final BindingResultUtils bindingResultUtils;
    private final MessageSource messageSource;


    public ResponseEntity sendItemResponseByCond(ItemSearchCond itemSearchCond, BindingResult bindingResult, Pageable pageable) {
        //바이딩 에러
        if (bindingResult.hasErrors()) {
            ApiResponseBody<Object> body = ApiResponseBody.builder().apiError(new ApiError(bindingResult)).data(null).message("binding error").build();
            return ResponseEntity.badRequest().body(body);
        }

        //서비스 에러
        if (itemSearchCond.getPriceMin() != null && itemSearchCond.getPriceMax() != null) {

            if (itemSearchCond.getPriceMin() > itemSearchCond.getPriceMax()) {
                String message = messageSource.getMessage("InvalidRange", null, null);
                ApiResponseBody<Object> body = ApiResponseBody.builder()
                        .apiError(new ApiError("priceMin", message))
                        .data(null)
                        .message("has error").build();
                return ResponseEntity.badRequest().body(body);
            }
        }

        if (itemSearchCond.getQuantityMin() != null && itemSearchCond.getQuantityMax() != null) {
            if (itemSearchCond.getQuantityMin() > itemSearchCond.getQuantityMax()) {
                String message = messageSource.getMessage("InvalidRange", null, null);
                ApiResponseBody<Object> body = ApiResponseBody.builder()
                        .apiError(new ApiError("quantityMin", message))
                        .data(null)
                        .message("has error").build();
                return ResponseEntity.badRequest().body(body);
            }
        }

        //정상 흐름
        Page<ItemResponse> itemResponseList = itemRepository.getItemResponseByCond(itemSearchCond, pageable);
        ApiResponseBody<Object> body = ApiResponseBody.builder()
                .data(itemResponseList)
                .apiError(null)
                .message("ok").build();
        return ResponseEntity.ok(body);
    }

    public ResponseEntity sendItemDetailPageByItemId(Long itemId) {
        //잘못된 값
        if (itemId == null || itemId < 0) {
            String message = messageSource.getMessage("Min", new Long[]{itemId, 0L}, null);
            ApiError error = new ApiError("id", message);
            ApiResponseBody<Object> bod = ApiResponseBody.builder()
                    .data(null)
                    .message(message)
                    .apiError(error)
                    .build();
            return ResponseEntity.badRequest().body(bod);
        }

        //DB에 없음
        Item findItem = itemRepository.findById(itemId).orElse(null);
        if (findItem == null) {
            String message = messageSource.getMessage("Nodata", null, null);
            ApiError error = new ApiError("id", message);
            ApiResponseBody<Object> body = ApiResponseBody.builder()
                    .data(null)
                    .message(message)
                    .apiError(error)
                    .build();
            return ResponseEntity.badRequest().body(body);
        }

        //정상 흐름
        ApiResponseBody<Object> body = ApiResponseBody.builder()
                .data(findItem)
                .apiError(null)
                .message("ok")
                .build();

        return ResponseEntity.ok(body);
    }
}
