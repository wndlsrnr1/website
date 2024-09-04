package com.website.repository.review;

import com.website.common.repository.review.ReviewRepository;
import com.website.config.jpa.JpaConfig;
import com.website.common.repository.item.ItemRepository;
import com.website.common.repository.model.item.Item;
import com.website.common.repository.user.model.User;
import com.website.common.repository.purchases.PurchasesRepository;
import com.website.common.repository.purchases.model.OrderStatus;
import com.website.common.repository.purchases.model.Purchases;
import com.website.common.repository.review.model.Review;
import com.website.common.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
@ActiveProfiles("local")
class ReviewRepositoryJpaTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PurchasesRepository purchasesRepository;

    Purchases purchases;

    @BeforeEach
    public void init() {

        Item item = Item.builder().name("item1").build();
        User user = User.builder().name("user1").build();

        purchases = purchasesRepository.findById(1L).orElse(
                purchasesRepository.save(Purchases.builder().item(item).user(user).status(OrderStatus.DELIVERED).build())
        );
    }

    // c
    @Test
    @DisplayName("[Review] create")
    public void create() throws Exception {
        // Given



        // When

        Review savedReview = reviewRepository.save(
                Review.builder().purchases(purchases).star(3).content("testContent").build()
        );

        // Then
        Long savedReviewId = savedReview.getId();
        Optional<Review> foundReview = reviewRepository.findById(savedReviewId);
        assertThat(foundReview).isPresent();
        assertThat(foundReview.get().getContent()).isEqualTo("testContent");
        assertThat(foundReview.get().getPurchases().getUser().getName()).isEqualTo("user1");
    }

    // r

    // u

    // d

}