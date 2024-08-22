package com.website.service.review;

import com.website.exception.*;
import com.website.repository.common.PageResult;
import com.website.repository.model.user.User;
import com.website.repository.purchases.PurchasesRepository;
import com.website.repository.purchases.model.OrderStatus;
import com.website.repository.purchases.model.Purchases;
import com.website.repository.review.ReviewRepository;
import com.website.repository.review.model.Review;
import com.website.service.common.model.PageResultDto;
import com.website.service.review.model.ReviewCreateDto;
import com.website.service.review.model.ReviewDto;
import com.website.service.review.model.ReviewSearchCriteria;
import com.website.service.review.model.ReviewUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PurchasesRepository purchasesRepository;

    @Transactional
    public ReviewDto registerReview(ReviewCreateDto dto, Long userId) {
        // Validate purchase
        Purchases purchase = purchasesRepository.findById(dto.getPurchasesId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BAD_REQUEST, "cannot found purchases. purchasesId = " + dto.getPurchasesId()));

        // validation
        if (!purchase.getUser().getId().equals(userId)) {
            throw new UnauthorizedActionException(ErrorCode.BAD_REQUEST, "user not qualified. userId = " + userId + " purchasesId = " + dto.getPurchasesId());
        }

        // already exist
        if (reviewRepository.existsByPurchases(purchase)) {
            throw new AlreadyExistsException(
                    ErrorCode.BAD_REQUEST,
                    "purchases already exists. userId = " + userId + " purchasesId = " + purchase.getId() + " reviewId = " + purchase.getUser().getId()
            );
        }

        // !(valid period)
        if (!(purchase.getStatus().equals(OrderStatus.DELIVERED)
                && Duration.between(purchase.getUpdatedAt(), LocalDateTime.now()).toDays() < 7)
        ) {
            throw new DateExpiredException(ErrorCode.BAD_REQUEST, "update not qualified. userId = " + userId + " purchasesId = " + dto.getPurchasesId());
        }

        // Create review
        Review review = new Review();
        review.setPurchases(purchase);
        review.setContent(dto.getContent());
        review.setStar(dto.getStar());

        // Save review
        Review savedReview = reviewRepository.save(review);
        log.info("savedReview = {}", savedReview);
        return ReviewDto.of(savedReview);
    }

    @Transactional(readOnly = true)
    public ReviewDto getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BAD_REQUEST, "review found found. reviewId = " + reviewId));
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

    // todo: handle UnauthorizedActionException.
    @Transactional
    public ReviewDto updateReview(ReviewUpdateDto dto, Long userId) {
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

        Long foundUserId = user.getId();

        if (!foundUserId.equals(userId)) {
            throw new UnauthorizedActionException(ErrorCode.BAD_REQUEST, "user not qualified. requested userId = " + userId);
        }

        // purchases 1주일 넘으면 수정 불가
        if (!(purchases.getStatus() == OrderStatus.DELIVERED
                        && Duration.between(purchases.getUpdatedAt(), LocalDateTime.now()).toDays() < 7)) {
            throw new DateExpiredException(ErrorCode.BAD_REQUEST, "update date expired. userId = " + userId + " purchasesId = " + purchases.getId());
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
