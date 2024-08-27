package com.website.repository.review;

import com.website.config.jpa.JpaConfig;
import com.website.repository.common.PageResult;
import com.website.repository.item.ItemRepository;
import com.website.repository.model.item.Item;
import com.website.repository.user.model.User;
import com.website.repository.purchases.PurchasesRepository;
import com.website.repository.purchases.model.OrderStatus;
import com.website.repository.purchases.model.Purchases;
import com.website.repository.review.model.Review;
import com.website.repository.review.model.ReviewSortType;
import com.website.repository.user.UserRepository;
import com.website.service.review.model.ReviewSearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfig.class, CustomReviewRepositoryImpl.class})
@ActiveProfiles("local")
class ReviewRepositoryPagingTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    PurchasesRepository purchasesRepository;

    Item item1;
    Item item2;

    User user1;
    User user2;
    User user3;
    User user4;
    User user5;

    @BeforeEach
    public void setUp() {
        //user 저장
        user1 = userRepository.save(
                User.builder().email("test1@naver.com").name("test1").name("name1").password("password1").build()
        );
        user2 = userRepository.save(
                User.builder().email("test2@naver.com").name("test2").name("name2").password("password2").build()
        );
        user3 = userRepository.save(
                User.builder().email("test3@naver.com").name("test3").name("name3").password("password3").build()
        );
        user4 = userRepository.save(
                User.builder().email("test4@naver.com").name("test4").name("name4").password("password4").build()
        );


        //item 저장
        item1 = itemRepository.save(
                Item.builder().nameKor("itemName1").status("good").build()
        );
        item2 = itemRepository.save(
                Item.builder().nameKor("itemName2").status("good").build()
        );


        //review 저장
        Purchases purchases1 = purchasesRepository.save(
                Purchases.builder().item(item1).user(user1)
                        .address("toHome").status(OrderStatus.DELIVERED)
                        .discount(10).notes("purchases1")
                        .orderNumber(UUID.randomUUID().toString()).totalAmount(1).build()
        );
        Purchases purchases2 = purchasesRepository.save(
                Purchases.builder().item(item1).user(user2)
                        .address("toHome").status(OrderStatus.DELIVERED)
                        .discount(10).notes("purchases1")
                        .orderNumber(UUID.randomUUID().toString()).totalAmount(1).build()
        );
        Purchases purchases3 = purchasesRepository.save(
                Purchases.builder().item(item1).user(user3)
                        .address("toHome").status(OrderStatus.DELIVERED)
                        .discount(10).notes("purchases1")
                        .orderNumber(UUID.randomUUID().toString()).totalAmount(1).build()
        );
        Purchases purchases4 = purchasesRepository.save(
                Purchases.builder().item(item1).user(user4)
                        .address("toHome").status(OrderStatus.DELIVERED)
                        .discount(10).notes("purchases1")
                        .orderNumber(UUID.randomUUID().toString()).totalAmount(1).build()
        );
        Purchases purchases5 = purchasesRepository.save(
                Purchases.builder().item(item2).user(user5)
                        .address("toHome").status(OrderStatus.DELIVERED)
                        .discount(10).notes("purchases1")
                        .orderNumber(UUID.randomUUID().toString()).totalAmount(1).build()
        );

        reviewRepository.save(Review.builder().star(5).content("content1").purchases(purchases1).build());
        reviewRepository.save(Review.builder().star(5).content("content2").purchases(purchases2).build());
        reviewRepository.save(Review.builder().star(3).content("content3").purchases(purchases3).build());
        reviewRepository.save(Review.builder().star(3).content("content4").purchases(purchases4).build());
        reviewRepository.save(Review.builder().star(1).content("content5").purchases(purchases5).build());
    }

    @Test
    @DisplayName("[reviewSearch] recent")
    public void reviewSearchTest1() throws Exception {
        // Given

        ReviewSearchCriteria criteria = ReviewSearchCriteria.builder()
                .size(2)
                .sortType(ReviewSortType.RECENT)
                .withTotalCount(true)
                .itemId(item1.getId())
                .build();

        // When

        PageResult<Review> result = reviewRepository.search(criteria);

        // Then
        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getItems().get(0).getContent()).isEqualTo("content4");
        assertThat(result.getTotalCount()).isEqualTo(4);

        // Given - find next
        String nextSearchAfter = result.getNextSearchAfter();
        criteria.setNextSearchAfter(nextSearchAfter);

        // When
        result = reviewRepository.search(criteria);

        // Then
        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getItems().get(0).getContent()).isEqualTo("content2");

        // Given - find next (Empty)
        nextSearchAfter = result.getNextSearchAfter();
        criteria.setNextSearchAfter(nextSearchAfter);

        // When
        result = reviewRepository.search(criteria);
        assertThat(result.getItems()).isEmpty();
        assertThat(result.getNextSearchAfter()).isNull();
        assertThat(result.getTotalCount()).isEqualTo(4);
    }

    @Test
    @DisplayName("[reviewSearch] STAR_ASC")
    public void reviewSearchTestV2() throws Exception {
        // Given

        ReviewSearchCriteria criteria = ReviewSearchCriteria.builder()
                .size(2)
                .sortType(ReviewSortType.STAR_ASC)
                .withTotalCount(true)
                .itemId(item1.getId())
                .build();

        // When

        PageResult<Review> result = reviewRepository.search(criteria);

        // Then

        assertThat(result.getItems().get(0).getContent()).isEqualTo("content4");
        assertThat(result.getTotalCount()).isEqualTo(4);
        assertThat(result.getItems()).hasSize(2);

        // Given - find next
        String nextSearchAfter = result.getNextSearchAfter();
        criteria.setNextSearchAfter(nextSearchAfter);

        // When
        result = reviewRepository.search(criteria);

        // Then
        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getItems().get(0).getContent()).isEqualTo("content2");

        // Given - find next (Empty)
        nextSearchAfter = result.getNextSearchAfter();
        criteria.setNextSearchAfter(nextSearchAfter);

        // When
        result = reviewRepository.search(criteria);
        assertThat(result.getItems()).isEmpty();
        assertThat(result.getNextSearchAfter()).isNull();
        assertThat(result.getTotalCount()).isEqualTo(4);
    }

    @Test
    @DisplayName("[reviewSearch] STAR_DESC")
    public void reviewSearchTestV3() throws Exception {
        // Given

        ReviewSearchCriteria criteria = ReviewSearchCriteria.builder()
                .size(2)
                .sortType(ReviewSortType.STAR_DESC)
                .withTotalCount(true)
                .itemId(item1.getId())
                .build();

        // When

        PageResult<Review> result = reviewRepository.search(criteria);

        // Then

        assertThat(result.getItems().get(0).getContent()).isEqualTo("content2");
        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getTotalCount()).isEqualTo(4);

        // Given - find next11

        String nextSearchAfter = result.getNextSearchAfter();
        criteria.setNextSearchAfter(nextSearchAfter);

        // When
        result = reviewRepository.search(criteria);

        // Then
        assertThat(result.getItems().get(0).getContent()).isEqualTo("content4");
        assertThat(result.getTotalCount()).isEqualTo(4);

        // Given - find next. empty.

        nextSearchAfter = result.getNextSearchAfter();
        criteria.setNextSearchAfter(nextSearchAfter);


        // When
        result = reviewRepository.search(criteria);

        // Then
        assertThat(result.getItems()).isEmpty();
        assertThat(result.getTotalCount()).isEqualTo(4);
        assertThat(result.getNextSearchAfter()).isNull();

    }


}