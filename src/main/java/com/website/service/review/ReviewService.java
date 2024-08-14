package com.website.service.review;

import com.website.exception.ClientException;
import com.website.exception.ErrorCode;
import com.website.repository.item.ItemRepository;
import com.website.repository.model.item.Item;
import com.website.repository.model.user.User;
import com.website.repository.review.ReviewRepository;
import com.website.repository.review.model.Review;
import com.website.repository.user.UserRepository;
import com.website.service.item.ItemValidator;
import com.website.service.review.model.ReviewCreateDto;
import com.website.service.review.model.ReviewDto;
import com.website.service.review.model.ReviewRequestDto;
import com.website.service.review.model.ReviewUpdateDto;
import com.website.service.user.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserValidator userValidator;
    private final ItemValidator itemValidator;

    // create
    @Transactional
    public ReviewDto registerReview(ReviewCreateDto dto) {
        User user = userValidator.validateAndGet(dto.getUserId());
        Item item = itemValidator.validateAndGet(dto.getItemId());

        Review review = Review.builder()
                .item(item)
                .user(user)
                .star(dto.getStar())
                .content(dto.getContent())
                .build();

        Review savedReview = reviewRepository.save(review);
        return ReviewDto.of(savedReview);
    }

    // read
    @Transactional(readOnly = true)
    public ReviewDto getReviewById(Long reviewId) {
        if (reviewId == null) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "reviewId is null");
        }

        Review foundReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ClientException(ErrorCode.BAD_REQUEST, "review not found. reviewId = " + reviewId));

        return ReviewDto.of(foundReview);
    }

    @Transactional(readOnly = true)
    public ReviewDto getReviewByUserIdAndItemId(Long userId, Long itemId) {
        User user = userValidator.validateAndGet(userId);
        Item item = itemValidator.validateAndGet(itemId);

        Review foundReview = reviewRepository.findByUserAndItem(user, item).orElseThrow(() ->
                new ClientException(ErrorCode.BAD_REQUEST, "review not found. userId = " + userId + ", itemId = " + itemId));

        return ReviewDto.of(foundReview);
    }

    // update
    @Transactional
    public ReviewDto updateReview(ReviewUpdateDto dto) {
        if (dto.getId() == null) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "reviewId is null. review = " + dto);
        }

        Review review = reviewRepository.findById(dto.getId()).orElseThrow(
                () -> new ClientException(ErrorCode.BAD_REQUEST, "review not found reviewId = " + dto.getId())
        );

        review.setContent(dto.getContent());
        review.setStar(dto.getStar());
        Review updatedReview = reviewRepository.save(review);
        return ReviewDto.of(updatedReview);
    }

    //delete
    @Transactional
    public void removeReview(Long userId, Long itemId) {
        User user = userValidator.validateAndGet(userId);
        Item item = itemValidator.validateAndGet(itemId);

        Review foundReview = reviewRepository.findByUserAndItem(user, item).orElseThrow(() ->
                new ClientException(ErrorCode.BAD_REQUEST, "review not found. userId = " + userId + ", itemId = " + itemId));

        reviewRepository.delete(foundReview);
    }
}
