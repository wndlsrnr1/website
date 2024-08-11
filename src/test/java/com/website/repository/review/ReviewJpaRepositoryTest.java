package com.website.repository.review;

import com.website.config.jpa.JpaConfig;
import com.website.repository.item.ItemRepository;
import com.website.repository.model.item.Item;
import com.website.repository.model.item.review.Review;
import com.website.repository.model.user.User;
import com.website.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JpaTest, Jpa Test는 Entity, JpaRepository 외에 인지하지 못하므로 설정과 기타 Repository Import
 * local profile에서 실행
 * c -> r -> u -> d 순서로 테스트 진행
 */
@DataJpaTest
@Import({CustomReviewRepositoryImpl.class, JpaConfig.class})
@ActiveProfiles("local")
class ReviewJpaRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ReviewRepository reviewRepository;
    /**
     * 검증할 Entity 제외 setUp
     */
    private User user;

    private Item item;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .name("test")
                .email("test@users.com")
                .password("password")
                .build();

        //userId 얻으려고 저장 한번함
        user = userRepository.save(user);

        item = Item.builder()
                .user(user)
                .status("good")
                .name("name")
                .nameKor("asdf")
                .build();

        item = itemRepository.save(item);
    }

    // create
    @Test
    @DisplayName("Creation Test")
    public void reviewRepository_create() throws Exception {
        //given
        Review review = Review.builder()
                .item(item)
                .user(user)
                .star(4)
                .content("test")
                .build();

        //when
        Review savedReview = reviewRepository.save(review);

        //then
        assertThat(savedReview.getId()).isNotNull();
        assertThat(savedReview.getContent()).isEqualTo("test");
        assertThat(savedReview.getItem().getName()).isEqualTo("name");
        assertThat(savedReview.getUser().getName()).isEqualTo("test");
        assertThat(savedReview.getStar()).isEqualTo(4);
        assertThat(savedReview.getCreatedAt()).isNotNull();
        assertThat(savedReview.getUpdatedAt()).isNotNull();
    }

    // read

    @Test
    @DisplayName("Read Test - findByUser")
    public void reviewRepository_findByUser() throws Exception {
        //given
        Review review = Review.builder()
                .item(item)
                .user(user)
                .star(4)
                .content("test")
                .build();
        Review savedReview = reviewRepository.save(review);

        //when
        List<Review> findReview = reviewRepository.findByUser(user);

        //then
        assertThat(findReview).hasSize(1);
        assertThat(findReview.get(0).getUser().getName()).isEqualTo("test");
        assertThat(findReview.get(0).getItem().getName()).isEqualTo("name");
        assertThat(findReview.get(0).getStar()).isEqualTo(4);
    }

    @Test
    @DisplayName("Read Test - findByProduct")
    public void reviewRepository_findByProduct() throws Exception {
        //given
        Review review = Review.builder()
                .item(item)
                .user(user)
                .star(4)
                .content("test")
                .build();
        Review savedReview = reviewRepository.save(review);

        //when
        List<Review> findReview = reviewRepository.findByItem(item);

        //then
        assertThat(findReview).hasSize(1);
        assertThat(findReview.get(0).getUser().getName()).isEqualTo("test");
        assertThat(findReview.get(0).getItem().getName()).isEqualTo("name");
        assertThat(findReview.get(0).getStar()).isEqualTo(4);
    }

    @Test
    @DisplayName("Read Test - findByUserAndProduct")
    public void reviewRepository_findByUserAndProduct() throws Exception {
        //given
        Review review = Review.builder()
                .item(item)
                .user(user)
                .star(4)
                .content("test")
                .build();
        Review savedReview = reviewRepository.save(review);

        //when
        Optional<Review> findReview = reviewRepository.findByUserAndItem(user, item);

        //then
        assertThat(findReview).isPresent();
        assertThat(findReview.get().getUser().getId()).isEqualTo(user.getId());
        assertThat(findReview.get().getItem().getId()).isEqualTo(item.getId());
        assertThat(findReview.get().getStar()).isEqualTo(4);
    }

    // update
    @Test
    @DisplayName("Update Test - update content and star")
    public void reviewRepository_updateReview() throws Exception {
        //given
        Review review = Review.builder()
                .item(item)
                .user(user)
                .star(4)
                .content("test")
                .build();
        Review savedReview = reviewRepository.save(review);

        //when
        savedReview.setContent("test2");
        savedReview.setStar(5);
        reviewRepository.save(savedReview);

        //then
        Review findReview = reviewRepository.findByUserAndItem(user, item).get();
        assertThat(findReview.getId()).isEqualTo(savedReview.getId());
        assertThat(findReview.getUser().getId()).isEqualTo(user.getId());
        assertThat(findReview.getItem().getId()).isEqualTo(item.getId());
        assertThat(findReview.getStar()).isEqualTo(5);
        assertThat(findReview.getContent()).isEqualTo("test2");

    }

    // delete
    @Test
    @DisplayName("Delete Test - byId")
    void reviewRepository_deleteReview() {
        //given
        Review review = Review.builder()
                .item(item)
                .user(user)
                .star(4)
                .content("test")
                .build();
        Review savedReview = reviewRepository.save(review);

        //when
        reviewRepository.deleteById(savedReview.getId());


        Optional<Review> findReview = reviewRepository.findByUserAndItem(user, item);
        assertThat(findReview).isNotPresent();
        findReview = reviewRepository.findById(review.getId());
        assertThat(findReview).isNotPresent();
    }

}