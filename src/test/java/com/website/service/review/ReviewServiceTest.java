package com.website.service.review;

import com.website.exception.ClientException;
import com.website.exception.ErrorCode;
import com.website.repository.model.item.Item;
import com.website.repository.model.user.User;
import com.website.repository.review.ReviewRepository;
import com.website.repository.review.model.Review;
import com.website.service.item.ItemValidator;
import com.website.service.review.model.ReviewCreateDto;
import com.website.service.review.model.ReviewDto;
import com.website.service.user.UserValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("local")
@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {ReviewService.class})
class ReviewServiceTest {

    @MockBean
    ReviewRepository reviewRepository;

    @MockBean
    UserValidator userValidator;

    @MockBean
    ItemValidator itemValidator;

    @Autowired
    ReviewService reviewService;

    private Long userId;
    private Long itemId;
    private User user;
    private Item item;
    private Review review;
    private ReviewCreateDto reviewDto;

    @BeforeEach
    void setUp() {
        userId = 1L;
        itemId = 1L;
        user = User.builder().id(userId).build();
        item = Item.builder().id(itemId).build();
        review = Review.builder()
                .user(user)
                .item(item)
                .build();

        reviewDto = ReviewCreateDto.builder()
                .userId(userId)
                .itemId(itemId)
                .build();
    }

    /**
     * return값 모두 넣어 줘야함.
     * Exception 발생 상황 적어줘야함
     * 몇번 실행 되는 지 적어야함.
     */
    //register
    @Test
    @DisplayName("Review register - success")
    public void registerReviewTest() throws Exception {
        //given
        when(userValidator.validateAndGet(userId)).thenReturn(user);
        when(itemValidator.validateAndGet(itemId)).thenReturn(item);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        //when
        ReviewDto reviewDto = reviewService.registerReview(this.reviewDto);

        //then
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(itemValidator, times(1)).validateAndGet(itemId);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    @DisplayName("Review register - userId is null")
    public void registerReviewTest1() throws Exception {
        //given
        doThrow(new ClientException(ErrorCode.BAD_REQUEST, "userId is null"))
                .when(userValidator).validateAndGet(null);

        //when

        ClientException clientException = assertThrows(ClientException.class, () -> userValidator.validateAndGet(null));

        //then
        assertThat(clientException.getServerMessage()).contains("userId is null");
        verify(userValidator, times(1)).validateAndGet(null);
        verify(itemValidator, times(0)).validateAndGet(any(Long.class));
        verify(reviewRepository, times(0)).save(any(Review.class));
    }

    @Test
    @DisplayName("Review register - user not found")
    public void registerReviewTest2() throws Exception {
        //given
        doThrow(new ClientException(ErrorCode.BAD_REQUEST, "user not found. userId=" + userId))
                .when(userValidator).validateAndGet(userId);

        //when
        ClientException exception = assertThrows(ClientException.class, () -> userValidator.validateAndGet(userId));
        assertThat(exception.getServerMessage()).contains("found. userId=" + userId);

        //then
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(itemValidator, times(0)).validateAndGet(any(Long.class));
        verify(reviewRepository, times(0)).save(any(Review.class));
    }

    @Test
    @DisplayName("Review register - item is null")
    public void registerReviewTest3() throws Exception {
        // 이런 파라미터로 실행 하였을때 이것을 리턴 해라.
        // given
        when(userValidator.validateAndGet(userId)).thenReturn(user);
        doThrow(new ClientException(ErrorCode.BAD_REQUEST, "item is null."))
                .when(itemValidator).validateAndGet(null);

        // 실행
        //when
        ClientException exception = assertThrows(ClientException.class, () -> reviewService.removeReview(userId, null));
        assertThat(exception.getServerMessage()).contains("item is null");

        // 검증
        //then
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(itemValidator, times(1)).validateAndGet(null);
        verify(reviewRepository, times(0)).save(any(Review.class));
    }

    @Test
    @DisplayName("Review register - itemId not found")
    public void registerReviewTest4() throws Exception {
        //given
        when(userValidator.validateAndGet(userId)).thenReturn(null);
        doThrow(new ClientException(ErrorCode.BAD_REQUEST, "itemId not found"))
                .when(itemValidator).validateAndGet(itemId);

        //when
        ClientException exception = assertThrows(ClientException.class, () -> reviewService.registerReview(reviewDto));

        //then
        assertThat(exception.getServerMessage()).contains("itemId not found");
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(itemValidator, times(1)).validateAndGet(itemId);
        verify(reviewRepository, times(0)).save(any(Review.class));
    }

    //find1

    //find2

    //update

    //delete

}