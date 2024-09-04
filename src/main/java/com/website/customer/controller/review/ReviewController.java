package com.website.customer.controller.review;

import com.website.config.auth.LoginUser;
import com.website.config.auth.ServiceUser;
import com.website.common.controller.model.ApiResponse;
import com.website.common.controller.model.PageResultResponse;
import com.website.customer.controller.review.model.ReviewCreateRequest;
import com.website.customer.controller.review.model.ReviewResponse;
import com.website.customer.controller.review.model.ReviewUpdateRequest;
import com.website.common.repository.review.model.ReviewSortType;
import com.website.common.service.model.PageResultDto;
import com.website.customer.service.review.ReviewService;
import com.website.customer.service.review.model.ReviewCreateDto;
import com.website.customer.service.review.model.ReviewDto;
import com.website.customer.service.review.model.ReviewSearchCriteria;
import com.website.customer.service.review.model.ReviewUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    // Create a new review
    @LoginUser
    @PostMapping
    public ApiResponse<ReviewResponse> createReview(
            @AuthenticationPrincipal ServiceUser serviceUser,
            @RequestBody @Valid ReviewCreateRequest request
    ) {
        ReviewCreateDto dto = request.toDto();
        ReviewDto reviewDto = reviewService.registerReview(dto, serviceUser.getId());
        ReviewResponse response = ReviewResponse.of(reviewDto);
        return ApiResponse.<ReviewResponse>success(response);
    }

    // Get a specific review by ID
    @GetMapping("/{reviewId}")
    public ApiResponse<ReviewResponse> getReview(
            @PathVariable(value = "reviewId") Long reviewId
    ) {
        ReviewDto reviewById = reviewService.getReviewById(reviewId);
        ReviewResponse response = ReviewResponse.of(reviewById);
        return ApiResponse.success(response);
    }

    // List all reviews, with optional search and pagination
    @GetMapping
    public ApiResponse<PageResultResponse<ReviewResponse>> searchReviews(
            @RequestParam @Min(1) Integer size,
            @RequestParam(required = false) String nextSearchAfter,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false, defaultValue = "false") boolean withTotalCount,
            @RequestParam ReviewSortType sortType
    ) {
        ReviewSearchCriteria dto = ReviewSearchCriteria.builder()
                .itemId(itemId)
                .userId(userId)
                .nextSearchAfter(nextSearchAfter)
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

    @LoginUser
    @GetMapping("/me")
    public ApiResponse<PageResultResponse<ReviewResponse>> searchReviewsMe(
            @AuthenticationPrincipal ServiceUser serviceUser,
            @RequestParam @Min(1) Integer size,
            @RequestParam(required = false) String nextSearchAfter,
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false, defaultValue = "false") boolean withTotalCount,
            @RequestParam ReviewSortType sortType
    ) {


        ReviewSearchCriteria dto = ReviewSearchCriteria.builder()
                .itemId(itemId)
                .userId(serviceUser.getId())
                .nextSearchAfter(nextSearchAfter)
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


    // Update an existing review
    @LoginUser
    @PatchMapping("/{reviewId}")
    public ApiResponse<ReviewResponse> updateReview(
            @AuthenticationPrincipal ServiceUser serviceUser,
            @RequestBody @Valid ReviewUpdateRequest request,
            @PathVariable(value = "reviewId") Long reviewId
    ) {
        ReviewUpdateDto dto = request.toDto(reviewId);
        ReviewDto reviewDto = reviewService.updateReview(dto, serviceUser.getId());
        ReviewResponse response = ReviewResponse.of(reviewDto);
        return ApiResponse.success(response);
    }

    // Delete a review by ID
    @LoginUser
    @DeleteMapping("/{reviewId}")
    public ApiResponse<Void> deleteReview(
            @AuthenticationPrincipal ServiceUser serviceUser,
            @PathVariable(value = "reviewId") Long reviewId
    ) {
        reviewService.removeReview(serviceUser.getId(), reviewId);
        return ApiResponse.<Void>success();
    }



}
