package com.website.controller.api.review;

import com.website.controller.api.common.model.ApiResponse;
import com.website.controller.api.common.model.PageResultResponse;
import com.website.controller.api.review.model.ReviewCreateRequest;
import com.website.controller.api.review.model.ReviewResponse;
import com.website.controller.api.review.model.ReviewUpdateRequest;
import com.website.repository.model.user.constance.UserConst;
import com.website.repository.review.model.ReviewSortType;
import com.website.service.common.model.PageResultDto;
import com.website.service.review.ReviewService;
import com.website.service.review.model.ReviewCreateDto;
import com.website.service.review.model.ReviewDto;
import com.website.service.review.model.ReviewSearchRequestDto;
import com.website.service.review.model.ReviewUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    //create
    @PostMapping("/reviews")
    public ApiResponse<ReviewResponse> createReview(
            //todo: implement injected authentication.
            HttpSession session,
            @RequestBody @Valid ReviewCreateRequest request
    ) {
        //todo: implement injected authentication.
        Long userId = (Long) session.getAttribute(UserConst.USER_ID);
        ReviewCreateDto dto = request.toDto(userId);
        ReviewDto reviewDto = reviewService.registerReview(dto);
        ReviewResponse response = ReviewResponse.of(reviewDto);
        return ApiResponse.success(response);
    }

    //read
    @GetMapping("/reviews/{reviewId}")
    public ApiResponse<ReviewResponse> getReview(
            @PathVariable(value = "reviewId") Long reviewId
    ) {
        ReviewDto reviewById = reviewService.getReviewById(reviewId);
        ReviewResponse response = ReviewResponse.of(reviewById);
        return ApiResponse.success(response);
    }

    @GetMapping("/reviews")
    public ApiResponse<PageResultResponse<ReviewResponse>> searchReviews(
            @RequestParam @Min(1) Integer size,
            @RequestParam(required = false) String searchAfter,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false, defaultValue = "false") boolean withTotalCount,
            @RequestParam ReviewSortType sortType
            ) {

        ReviewSearchRequestDto dto = ReviewSearchRequestDto.builder()
                .itemId(itemId)
                .userId(userId)
                .nextSearchAfter(searchAfter)
                .size(size)
                .sortType(sortType)
                .withTotalCount(withTotalCount)
                .build();

        PageResultDto<ReviewDto> resultDto = reviewService.searchReview(dto);
        PageResultResponse<ReviewResponse> resultResponse = PageResultResponse.<ReviewResponse>builder()
                .items(resultDto.getItems().stream().map(ReviewResponse::of).collect(Collectors.toList()))
                .nextSearchAfter(resultDto.getNextSearchAfter())
                .totalCount(resultDto.getTotalCount())
                .build();

        return ApiResponse.success(resultResponse);

    }

    //update
    @PostMapping("/items/{itemId}/update/reviews")
    public ApiResponse<ReviewResponse> updateReview(
            //todo: implement injected authentication.
            HttpSession session,
            @PathVariable(value = "itemId") Long itemId,
            @RequestBody @Valid ReviewUpdateRequest request
    ) {
        //todo: implement injected authentication.
        Long userId = (Long) session.getAttribute(UserConst.USER_ID);
        ReviewUpdateDto dto = request.toDto(userId, itemId);
        ReviewDto reviewDto = reviewService.updateReview(dto);
        ReviewResponse response = ReviewResponse.of(reviewDto);
        return ApiResponse.success(response);
    }

    //delete
    @DeleteMapping("/items/{itemId}/reviews")
    public ApiResponse<Void> deleteReview(
            HttpSession session, //todo: replace to injected value from security
            @PathVariable(value = "itemId") Long itemId
    ) {
        //todo: replace to injected value from security
        Long userId = (Long) session.getAttribute(UserConst.USER_ID);
        reviewService.removeReview(userId, itemId);
        return ApiResponse.<Void>success();
    }

}
