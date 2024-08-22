package com.website.service.review;

import com.website.exception.AlreadyExistsException;
import com.website.exception.DateExpiredException;
import com.website.exception.ResourceNotFoundException;
import com.website.exception.UnauthorizedActionException;
import com.website.repository.common.PageResult;
import com.website.repository.model.item.Item;
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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {ReviewService.class})
@ActiveProfiles("local")
class ReviewServiceTest {

    @MockBean
    ReviewRepository reviewRepository;

    @MockBean
    PurchasesRepository purchasesRepository;

    @Autowired
    ReviewService reviewService;

    private Item item;
    private User user;
    private Purchases purchases;
    private Review review;
    private Review reviewParam;

    @BeforeEach
    void setUp() {
        item = Item.builder().id(1L).name("item1").build();
        user = User.builder().id(1L).email("user1@email.com").build();
        purchases = Purchases.builder()
                .id(1L)
                .item(item)
                .user(user)
                .status(OrderStatus.DELIVERED)
                .updatedAt(LocalDateTime.now().minusDays(6))
                .build();

        review = Review.builder().id(1L).star(4).content("content").purchases(purchases).build();
        reviewParam = Review.builder().star(4).content("content").purchases(purchases).build();
    }

    @Test
    @DisplayName("[reviewService][register][success]")
    public void registerReviewV1() throws Exception {
        // Given
        ReviewCreateDto dto = ReviewCreateDto.builder().purchasesId(1L)
                .star(4)
                .content("content")
                .build();

        when(purchasesRepository.findById(dto.getPurchasesId())).thenReturn(Optional.of(purchases));
        when(reviewRepository.existsByPurchases(purchases)).thenReturn(false);
        when(reviewRepository.save(reviewParam)).thenReturn(review);

        // When

        ReviewDto reviewDto = reviewService.registerReview(dto, user.getId());

        // Then

        verify(purchasesRepository, times(1)).findById(dto.getPurchasesId());
        verify(reviewRepository, times(1)).existsByPurchases(purchases);
        verify(reviewRepository, times(1)).save(reviewParam);
        assertThat(reviewDto.getContent()).isEqualTo(dto.getContent());
        assertThat(reviewDto.getId()).isEqualTo(review.getId());
    }

    @Test
    @DisplayName("[reviewService][register][cannot found purchases]")
    public void registerReviewV2() throws Exception {
        // Given
        ReviewCreateDto dto = ReviewCreateDto.builder().purchasesId(1L)
                .star(4)
                .content("content")
                .build();

        when(purchasesRepository.findById(dto.getPurchasesId())).thenReturn(Optional.empty());

        // When

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                reviewService.registerReview(dto, user.getId())
        );

        // Then
        assertThat(exception.getServerMessage()).contains("cannot found purchases");
        verify(purchasesRepository, times(1)).findById(dto.getPurchasesId());
        verify(reviewRepository, times(0)).existsByPurchases(any(Purchases.class));
        verify(reviewRepository, times(0)).save(any(Review.class));
    }

    @Test
    @DisplayName("[reviewService][register][user not qualified]")
    public void registerReviewV3() throws Exception {
        // Given
        Long userId = 2L;
        ReviewCreateDto dto = ReviewCreateDto.builder().purchasesId(1L)
                .star(4)
                .content("content")
                .build();

        when(purchasesRepository.findById(dto.getPurchasesId())).thenReturn(Optional.of(purchases));
        // When

        UnauthorizedActionException exception = assertThrows(UnauthorizedActionException.class,
                () -> reviewService.registerReview(dto, userId));

        // Then

        assertThat(exception.getServerMessage()).contains("user not qualified. userId");
        verify(purchasesRepository, times(1)).findById(dto.getPurchasesId());
        verify(reviewRepository, times(0)).existsByPurchases(any(Purchases.class));
        verify(reviewRepository, times(0)).save(any(Review.class));
    }

    @Test
    @DisplayName("[reviewService][register][already exists]")
    public void registerReviewV4() throws Exception {
        // Given
        ReviewCreateDto dto = ReviewCreateDto.builder().purchasesId(1L)
                .star(4)
                .content("content")
                .build();

        when(purchasesRepository.findById(dto.getPurchasesId())).thenReturn(Optional.of(purchases));
        when(reviewRepository.existsByPurchases(purchases)).thenReturn(true);

        // When

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
                () -> reviewService.registerReview(dto, user.getId()));

        // Then

        assertThat(exception.getServerMessage()).contains("purchases already exists. userId ");
        verify(purchasesRepository, times(1)).findById(dto.getPurchasesId());
        verify(reviewRepository, times(1)).existsByPurchases(purchases);
        verify(reviewRepository, times(0)).save(any(Review.class));
    }

    @Test
    @DisplayName("[reviewService][register][not delivered]")
    public void registerReviewV5() throws Exception {
        purchases.setStatus(OrderStatus.SHIPPED);
        //purchases.setUpdatedAt(LocalDateTime.now().minusDays(7));
        // Given
        ReviewCreateDto dto = ReviewCreateDto.builder().purchasesId(1L)
                .star(4)
                .content("content")
                .build();

        when(purchasesRepository.findById(dto.getPurchasesId())).thenReturn(Optional.of(purchases));
        when(reviewRepository.existsByPurchases(purchases)).thenReturn(false);

        // When

        DateExpiredException exception = assertThrows(DateExpiredException.class,
                () -> reviewService.registerReview(dto, user.getId()));

        // Then
        Assertions.assertThat(exception.getServerMessage()).contains("update not qualified. userId = ");
        verify(purchasesRepository, times(1)).findById(dto.getPurchasesId());
        verify(reviewRepository, times(1)).existsByPurchases(purchases);
        verify(reviewRepository, times(0)).save(any(Review.class));
    }

    @Test
    @DisplayName("[reviewService][register][out dated]")
    public void registerReviewV6() throws Exception {
        purchases.setUpdatedAt(LocalDateTime.now().minusDays(7));
        // Given
        ReviewCreateDto dto = ReviewCreateDto.builder().purchasesId(1L)
                .star(4)
                .content("content")
                .build();

        when(purchasesRepository.findById(dto.getPurchasesId())).thenReturn(Optional.of(purchases));
        when(reviewRepository.existsByPurchases(purchases)).thenReturn(false);

        // When

        DateExpiredException exception = assertThrows(DateExpiredException.class,
                () -> reviewService.registerReview(dto, user.getId()));

        // Then
        Assertions.assertThat(exception.getServerMessage()).contains("update not qualified. userId = ");
        verify(purchasesRepository, times(1)).findById(dto.getPurchasesId());
        verify(reviewRepository, times(1)).existsByPurchases(purchases);
        verify(reviewRepository, times(0)).save(any(Review.class));
    }

    @Test
    @DisplayName("[reviewService][get][success]")
    public void getReviewV1() throws Exception {
        // Given

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        // When

        ReviewDto reviewDto = reviewService.getReviewById(review.getId());

        // Then
        assertThat(reviewDto.getId()).isEqualTo(review.getId());
        assertThat(reviewDto.getStar()).isEqualTo(review.getStar());
        assertThat(reviewDto.getContent()).isEqualTo(review.getContent());
        verify(reviewRepository, times(1)).findById(review.getId());
    }

    @Test
    @DisplayName("[reviewService][get][review not found]")
    public void getReviewV2() throws Exception {
        // Given

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.empty());

        // When

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> reviewService.getReviewById(review.getId()));

        Assertions.assertThat(exception.getServerMessage()).contains("review found found. reviewId =");
        // Then
        verify(reviewRepository, times(1)).findById(review.getId());
    }

    @Test
    @DisplayName("[reviewService][search][success]")
    public void searchReviewV1() throws Exception {
        // Given

        ReviewSearchCriteria anyDto = ReviewSearchCriteria.builder().build();

        PageResult<Review> result = PageResult.<Review>builder()
                .items(List.of(review))
                .nextSearchAfter("abc")
                .totalCount(3L)
                .build();

        when(reviewRepository.search(anyDto)).thenReturn(result);

        // When

        PageResultDto<ReviewDto> pageResult = reviewService.searchReview(anyDto);

        // Then
        assertThat(pageResult.getItems().get(0).getId()).isEqualTo(result.getItems().get(0).getId());
        assertThat(pageResult.getTotalCount()).isEqualTo(result.getTotalCount());
    }

    @Test
    @DisplayName("[reviewService][update][success]")
    public void updateReviewV1() throws Exception {
        // Given

        ReviewUpdateDto updateDto = ReviewUpdateDto.builder().reviewId(review.getId())
                .content("updated")
                .star(1)
                .build();

        Review afterUpdatedReview = Review.builder()
                .id(review.getId())
                .star(updateDto.getStar())
                .content(updateDto.getContent())
                .purchases(review.getPurchases())
                .build();


        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        when(reviewRepository.save(review)).thenReturn(afterUpdatedReview);
        // When

        ReviewDto reviewDto = reviewService.updateReview(updateDto, user.getId());

        // Then
        assertThat(reviewDto.getId()).isEqualTo(review.getId());
        assertThat(reviewDto.getContent()).isEqualTo(review.getContent());
        assertThat(reviewDto.getStar()).isEqualTo(review.getStar());
        assertThat(reviewDto.getContent()).isEqualTo("updated");
    }

    @Test
    @DisplayName("[reviewService][remove][success]")
    public void removeReviewV1() throws Exception {
        // Given

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        // When

        reviewService.removeReview(user.getId(), review.getId());

        // Then
        verify(reviewRepository, times(1)).findById(review.getId());
        verify(reviewRepository, times(1)).delete(review);
    }

}