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
import com.website.service.review.model.ReviewSearchCriteria;
import com.website.service.review.model.ReviewUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Validated
public class ReviewController {

    private final ReviewService reviewService;

    // Create a new review
    @PostMapping
    public ApiResponse<ReviewResponse> createReview(
            HttpSession session,
            @RequestBody @Valid ReviewCreateRequest request
    ) {
        Long userId = (Long) session.getAttribute(UserConst.USER_ID); // replace with injected authentication in the future
        ReviewCreateDto dto = request.toDto();
        ReviewDto reviewDto = reviewService.registerReview(dto, userId);
        ReviewResponse response = ReviewResponse.of(reviewDto);
        return ApiResponse.success(response);
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
            @RequestParam(required = false) String searchAfter,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false, defaultValue = "false") boolean withTotalCount,
            @RequestParam ReviewSortType sortType
    ) {
        ReviewSearchCriteria dto = ReviewSearchCriteria.builder()
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

    // Update an existing review
    @PostMapping("/update")
    public ApiResponse<ReviewResponse> updateReview(
            HttpSession session,
            @RequestBody @Valid ReviewUpdateRequest request
    ) {
        Long userId = (Long) session.getAttribute(UserConst.USER_ID); // replace with injected authentication in the future
        ReviewUpdateDto dto = request.toDto();
        ReviewDto reviewDto = reviewService.updateReview(dto, userId);
        ReviewResponse response = ReviewResponse.of(reviewDto);
        return ApiResponse.success(response);
    }

    // Delete a review by ID
    @DeleteMapping("/{reviewId}")
    public ApiResponse<Void> deleteReview(
            HttpSession session,
            @PathVariable(value = "reviewId") Long reviewId
    ) {
        Long userId = (Long) session.getAttribute(UserConst.USER_ID); // replace with injected authentication in the future
        reviewService.removeReview(userId, reviewId);
        return ApiResponse.<Void>success();
    }


}
