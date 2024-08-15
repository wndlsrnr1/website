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
import com.website.service.review.model.ReviewUpdateDto;
import com.website.service.user.UserValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

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
    private ReviewCreateDto reviewCreateDto;

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

        reviewCreateDto = ReviewCreateDto.builder()
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
    @DisplayName("[Review register] - success")
    public void registerReviewTest() throws Exception {
        //given
        when(userValidator.validateAndGet(userId)).thenReturn(user);
        when(itemValidator.validateAndGet(itemId)).thenReturn(item);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        //when
        ReviewDto reviewDto = reviewService.registerReview(this.reviewCreateDto);

        //then
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(itemValidator, times(1)).validateAndGet(itemId);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    @DisplayName("[Review register] - userId is null")
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
    @DisplayName("[Review register] - user not found")
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
    @DisplayName("[Review register] - item is null")
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

    //when, thenReturn, doThrow, assertThrows, verify.
    @Test
    @DisplayName("[Review register] - itemId not found")
    public void registerReviewTest4() throws Exception {
        //given
        when(userValidator.validateAndGet(userId)).thenReturn(null);
        doThrow(new ClientException(ErrorCode.BAD_REQUEST, "itemId not found"))
                .when(itemValidator).validateAndGet(itemId);

        //when
        ClientException exception = assertThrows(ClientException.class, () -> reviewService.registerReview(reviewCreateDto));

        //then
        assertThat(exception.getServerMessage()).contains("itemId not found");
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(itemValidator, times(1)).validateAndGet(itemId);
        verify(reviewRepository, times(0)).save(any(Review.class));
    }

    @Test
    @DisplayName("[Review find] - user not found")
    public void getReviewByUserIdAndItemIdTestV1() throws Exception {
        //given
        doThrow(new ClientException(ErrorCode.BAD_REQUEST, "user not found"))
                .when(userValidator).validateAndGet(userId);

        //when
        ClientException exception = assertThrows(ClientException.class, () ->
                reviewService.getReviewByUserIdAndItemId(userId, itemId));

        //then
        assertThat(exception.getMessage()).contains("user not");
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(itemValidator, times(0)).validateAndGet(any(Long.class));
        verify(reviewRepository, times(0)).findByUserAndItem(any(User.class), any(Item.class));
    }

    @Test
    @DisplayName("[Review find] - item not found")
    public void getReviewByUserIdAndItemIdTestV2() throws Exception {
        //given
        when(userValidator.validateAndGet(userId)).thenReturn(user);
        doThrow(new ClientException(ErrorCode.BAD_REQUEST, "user not found"))
                .when(itemValidator).validateAndGet(itemId);

        //when
        ClientException exception = assertThrows(ClientException.class, () ->
                reviewService.getReviewByUserIdAndItemId(userId, itemId));

        //then
        assertThat(exception.getMessage()).contains("user not");
        verify(userValidator, times(1)).validateAndGet(any(Long.class));
        verify(itemValidator, times(1)).validateAndGet(itemId);
        verify(reviewRepository, times(0)).findByUserAndItem(any(User.class), any(Item.class));
    }

    @Test
    @DisplayName("[Review find] - review not found")
    public void getReviewByUserIdAndItemIdTestV3() throws Exception {
        //given
        when(userValidator.validateAndGet(userId)).thenReturn(user);
        when(itemValidator.validateAndGet(itemId)).thenReturn(item);
        when(reviewRepository.findByUserAndItem(user, item)).thenReturn(Optional.empty());

        //when
        ClientException exception = assertThrows(ClientException.class, () ->
                reviewService.getReviewByUserIdAndItemId(userId, itemId));

        //then
        assertThat(exception.getMessage()).contains("review not found. userId = ");
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(itemValidator, times(1)).validateAndGet(itemId);
        verify(reviewRepository, times(1)).findByUserAndItem(user, item);
    }

    @Test
    @DisplayName("[Review find] - success")
    public void getReviewByUserIdAndItemIdTestV4() throws Exception {
        //given
        when(userValidator.validateAndGet(userId)).thenReturn(user);
        when(itemValidator.validateAndGet(itemId)).thenReturn(item);
        when(reviewRepository.findByUserAndItem(user, item)).thenReturn(Optional.of(review));

        //when
        ReviewDto reviewDto = reviewService.getReviewByUserIdAndItemId(userId, itemId);

        //then
        assertThat(reviewDto.getUserId()).isEqualTo(review.getUser().getId());
        assertThat(reviewDto.getItemId()).isEqualTo(review.getItem().getId());
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(itemValidator, times(1)).validateAndGet(itemId);
        verify(reviewRepository, times(1)).findByUserAndItem(user, item);
    }

    //update
    @Test
    @DisplayName("[Review update] - success")
    public void updateReviewTestV1() throws Exception {
        ReviewUpdateDto updateDto = ReviewUpdateDto.builder()
                .id(1L)
                .content("updatedContent")
                .star(4)
                .build();

        Review beforeUpdate = Review.builder()
                .id(updateDto.getId())
                .item(item)
                .user(user)
                .build();

        Review afterUpdate = Review.builder()
                .id(updateDto.getId())
                .item(item)
                .user(user)
                .content(updateDto.getContent())
                .star(updateDto.getStar())
                .build();

        when(reviewRepository.findById(updateDto.getId())).thenReturn(Optional.of(beforeUpdate));
        when(reviewRepository.save(beforeUpdate)).thenReturn(afterUpdate);
        ReviewDto reviewDto = reviewService.updateReview(updateDto);

        assertThat(reviewDto.getId()).isEqualTo(updateDto.getId());
        assertThat(reviewDto.getContent()).isEqualTo(updateDto.getContent());
        assertThat(reviewDto.getStar()).isEqualTo(updateDto.getStar());

        verify(reviewRepository, times(1)).findById(updateDto.getId());
        verify(reviewRepository, times(1)).save(beforeUpdate);
    }

    @Test
    @DisplayName("[Review update] - review Id is null")
    public void updateReviewTestV2() throws Exception {
        ReviewUpdateDto updateDto = ReviewUpdateDto.builder()
                .id(null)
                .content("updatedContent")
                .star(4)
                .build();

        ClientException exception = assertThrows(ClientException.class,
                () -> reviewService.updateReview(updateDto)
        );

        assertThat(exception.getServerMessage()).contains("reviewId is null. review = ");

        verify(reviewRepository, times(0)).findById(any(Long.class));
        verify(reviewRepository, times(0)).save(any(Review.class));
    }

    @Test
    @DisplayName("[Review update] - review not found")
    public void updateReviewTestV3() throws Exception {
        //given
        ReviewUpdateDto updateDto = ReviewUpdateDto.builder()
                .id(1L)
                .content("updatedContent")
                .star(4)
                .build();

        //when
        ClientException exception = assertThrows(ClientException.class,
                () -> reviewService.updateReview(updateDto));

        assertThat(exception.getMessage()).contains("review not found reviewId = ");
        verify(reviewRepository, times(1)).findById(updateDto.getId());
        verify(reviewRepository, times(0)).save(any(Review.class));
    }

    //delete
    @Test
    @DisplayName("[Review remove] - success")
    public void removeReviewTestV1() throws Exception {
        //given
        when(userValidator.validateAndGet(userId)).thenReturn(user);
        when(itemValidator.validateAndGet(itemId)).thenReturn(item);
        when(reviewRepository.findByUserAndItem(user, item)).thenReturn(Optional.of(review));

        //when
        reviewService.removeReview(userId, itemId);

        //then
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(itemValidator, times(1)).validateAndGet(itemId);
        verify(reviewRepository, times(1)).findByUserAndItem(user, item);
        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    @DisplayName("[Review remove] - user validate fail")
    public void removeReviewTestV2() throws Exception {
        //given
        doThrow(new ClientException(ErrorCode.BAD_REQUEST, "some error in user validation"))
                .when(userValidator).validateAndGet(userId);

        //when
        ClientException ex = assertThrows(ClientException.class, () ->
                reviewService.removeReview(userId, itemId));

        //then
        assertThat(ex.getServerMessage()).contains("error in user");
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(itemValidator, times(0)).validateAndGet(any(Long.class));
        verify(reviewRepository, times(0)).findByUserAndItem(any(User.class), any(Item.class));
        verify(reviewRepository, times(0)).delete(any(Review.class));
    }

    @Test
    @DisplayName("[Review remove] - item validate fail")
    public void removeReviewTestV3() throws Exception {
        //given
        when(userValidator.validateAndGet(userId)).thenReturn(user);
        doThrow(new ClientException(ErrorCode.BAD_REQUEST, "some error in item validation"))
                .when(itemValidator).validateAndGet(itemId);

        //when
        ClientException ex = assertThrows(ClientException.class, () ->
                reviewService.removeReview(userId, itemId));

        //then
        assertThat(ex.getServerMessage()).contains("error in item");
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(itemValidator, times(1)).validateAndGet(any(Long.class));
        verify(reviewRepository, times(0)).findByUserAndItem(any(User.class), any(Item.class));
        verify(reviewRepository, times(0)).delete(any(Review.class));
    }

    @Test
    @DisplayName("[Review remove] - review not found")
    public void removeReviewTestV4() throws Exception {
        //given
        when(userValidator.validateAndGet(userId)).thenReturn(user);
        when(itemValidator.validateAndGet(itemId)).thenReturn(item);
        when(reviewRepository.findByUserAndItem(user, item)).thenReturn(Optional.empty());

        //when
        ClientException ex = assertThrows(ClientException.class, () ->
                reviewService.removeReview(userId, itemId));

        //then
        assertThat(ex.getServerMessage()).contains("review not found. userId =");
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(itemValidator, times(1)).validateAndGet(any(Long.class));
        verify(reviewRepository, times(1)).findByUserAndItem(any(User.class), any(Item.class));
        verify(reviewRepository, times(0)).delete(any(Review.class));
    }

}