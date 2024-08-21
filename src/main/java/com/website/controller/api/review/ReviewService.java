package com.website.controller.api.review;

import com.website.exception.ErrorCode;
import com.website.exception.ResourceNotFoundException;
import com.website.exception.ServerException;
import com.website.exception.UnauthorizedActionException;
import com.website.repository.common.PageResult;
import com.website.repository.model.user.User;
import com.website.repository.purchases.PurchasesRepository;
import com.website.repository.purchases.model.Purchases;
import com.website.repository.review.ReviewRepository;
import com.website.repository.review.model.Review;
import com.website.service.common.model.PageResultDto;
import com.website.service.review.model.ReviewCreateDto;
import com.website.service.review.model.ReviewDto;
import com.website.service.review.model.ReviewSearchCriteria;
import com.website.service.review.model.ReviewUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PurchasesRepository purchasesRepository;

    @Transactional
    public ReviewDto registerReview(ReviewCreateDto dto) {
        // Validate purchase
        Purchases purchase = purchasesRepository.findById(dto.getPurchasesId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BAD_REQUEST, "cannot found purchases. purchasesId = " + dto.getPurchasesId()));

        // Create review
        Review review = new Review();
        review.setPurchases(purchase);
        review.setContent(dto.getContent());
        review.setStar(dto.getStar());

        // Save review
        Review savedReview = reviewRepository.save(review);
        return ReviewDto.of(savedReview);
    }

    @Transactional(readOnly = true)
    public ReviewDto getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BAD_REQUEST, "cannot found purchases. purchasesId = " + reviewId));
        return ReviewDto.of(review);
    }

    @Transactional(readOnly = true)
    public PageResultDto<ReviewDto> searchReview(ReviewSearchCriteria dto) {

        PageResult<Review> pageResult = reviewRepository.search(dto);

        return PageResultDto.<ReviewDto>builder()
                .nextSearchAfter(pageResult.getNextSearchAfter())
                .totalCount(pageResult.getTotalCount())
                .items(pageResult.getItems().stream().map(ReviewDto::of).collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public ReviewDto updateReview(ReviewUpdateDto dto) {
        // Fetch review
        Review review = reviewRepository.findById(dto.getReviewId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BAD_REQUEST, "review not found. reviewId = " + dto.getReviewId()));

        // Check permissions
        Purchases purchases = review.getPurchases();
        if (purchases == null) {
            throw new ServerException(ErrorCode.INTERNAL_SERVER_ERROR, "cannot found review's purchases. reviewId = " + review.getId());
        }

        User user = purchases.getUser();
        if (user == null) {
            throw new ServerException(ErrorCode.INTERNAL_SERVER_ERROR, "cannot found purchases's userId. reviewId = " + review.getId() + " purchasesId = " + purchases.getId());
        }
        Long userId = user.getId();


        if (!userId.equals(dto.getUserId())) {
            throw new UnauthorizedActionException(ErrorCode.BAD_REQUEST, "user not qualified. userId" + userId);
        }

        // Update review fields
        review.setContent(dto.getContent());
        review.setStar(dto.getStar());

        // Save updated review
        Review updatedReview = reviewRepository.save(review);
        return ReviewDto.of(updatedReview);
    }

    @Transactional
    public void removeReview(Long userId, Long reviewId) {

        // Fetch review
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BAD_REQUEST, "review not found. reviewId = " + reviewId));

        // Check ownership or permissions
        Purchases purchases = review.getPurchases();
        if (purchases == null) {
            throw new ServerException(ErrorCode.INTERNAL_SERVER_ERROR, "cannot found review's purchases. reviewId = " + review.getId());
        }

        User user = purchases.getUser();
        if (user == null) {
            throw new ServerException(ErrorCode.INTERNAL_SERVER_ERROR, "cannot found purchases's userId. reviewId = " + review.getId() + " purchasesId = " + purchases.getId());
        }

        if (!review.getPurchases().getUser().getId().equals(userId)) {
            throw new UnauthorizedActionException(ErrorCode.BAD_REQUEST, "user not qualified. userId" + userId);
        }

        // Delete review
        reviewRepository.delete(review);
    }
}
