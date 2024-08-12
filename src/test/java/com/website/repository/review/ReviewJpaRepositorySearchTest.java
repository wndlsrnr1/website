package com.website.repository.review;

import com.website.config.jpa.JpaConfig;
import com.website.repository.common.PageResult;
import com.website.repository.item.ItemRepository;
import com.website.repository.model.item.Item;
import com.website.repository.model.user.User;
import com.website.repository.review.model.Review;
import com.website.repository.review.model.ReviewSearchCriteria;
import com.website.repository.review.model.ReviewSortType;
import com.website.repository.user.UserRepository;
import com.website.utils.common.SearchAfterEncoder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.naming.directory.SearchResult;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfig.class, CustomReviewRepositoryImpl.class})
@ActiveProfiles("local")
public class ReviewJpaRepositorySearchTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReviewRepository reviewRepository;

    Item item1;
    Item item2;

    User user1;
    User user2;
    User user3;
    User user4;

    @BeforeEach
    public void setUp() {
        //user 저장
        user1 = userRepository.save(User.builder().name("user1").email("user1@email.com").address("user1address").password("user1password").build());
        user2 = userRepository.save(User.builder().name("user2").email("user2@email.com").address("user2address").password("user2password").build());
        user3 = userRepository.save(User.builder().name("user3").email("user3@email.com").address("user3address").password("user3password").build());
        user4 = userRepository.save(User.builder().name("user4").email("user4@email.com").address("user4address").password("user4password").build());

        //item 저장
        item1 = itemRepository.save(Item.builder().name("item1name").nameKor("item1nameKor").status("item1status").build());
        item2 = itemRepository.save(Item.builder().name("item2name").nameKor("item2nameKor").status("item2status").build());

        //review 저장
        reviewRepository.save(Review.builder().item(item1).user(user1).content("content1").star(1).build());
        reviewRepository.save(Review.builder().item(item1).user(user2).content("content2").star(1).build());
        reviewRepository.save(Review.builder().item(item1).user(user3).content("content3").star(2).build());
        reviewRepository.save(Review.builder().item(item1).user(user4).content("content4").star(2).build());

        reviewRepository.save(Review.builder().item(item2).user(user4).content("content5").star(4).build());
        reviewRepository.save(Review.builder().item(item2).user(user3).content("content6").star(4).build());
        reviewRepository.save(Review.builder().item(item2).user(user2).content("content7").star(3).build());
        reviewRepository.save(Review.builder().item(item2).user(user1).content("content8").star(3).build());
    }

    //item에 따라서

    //최신순
    @Test
    @DisplayName("reviewSearch - by Item - recent")
    public void reviewSearch_recent_byItem() throws Exception {
        //given
        ReviewSearchCriteria criteria = ReviewSearchCriteria.builder()
                .size(2)
                .sortType(ReviewSortType.RECENT)
                .withTotalCount(true)
                .item(item1)
                .build();

        // when
        PageResult<Review> result = reviewRepository.search(criteria);

        // then
        assertThat(result.getTotalCount()).isEqualTo(4);
        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getItems().get(0).getUser()).isEqualTo(user4);
        assertThat(result.getGetNextSearchAfter()).isNotNull();

        // search next page
        String nextSearchAfter = result.getGetNextSearchAfter();
        criteria.setNextSearchAfter(nextSearchAfter);
        result = reviewRepository.search(criteria);

        assertThat(result.getTotalCount()).isEqualTo(4);
        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getItems().get(0).getUser()).isEqualTo(user2);
        assertThat(result.getGetNextSearchAfter()).isNotNull();

        // search next page
        nextSearchAfter = result.getGetNextSearchAfter();
        criteria.setNextSearchAfter(nextSearchAfter);
        result = reviewRepository.search(criteria);

        assertThat(result.getTotalCount()).isEqualTo(4);
        assertThat(result.getItems()).isEmpty();
        assertThat(result.getGetNextSearchAfter()).isNull();
    }

    //별점순
    @Test
    @DisplayName("reviewSearch - by item - star")
    public void reviewSearch_star_byItem() throws Exception {
        //given
        ReviewSearchCriteria criteria = ReviewSearchCriteria.builder()
                .size(2)
                .sortType(ReviewSortType.STAR)
                .withTotalCount(true)
                .item(item2)
                .build();

        //when
        PageResult<Review> result = reviewRepository.search(criteria);

        //then
        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getTotalCount()).isEqualTo(4);
        assertThat(result.getItems().get(0).getUser()).isEqualTo(user3);
        assertThat(result.getGetNextSearchAfter()).isNotNull();


        // next search

        String nextSearchAfter = result.getGetNextSearchAfter();
        criteria.setNextSearchAfter(nextSearchAfter);
        result = reviewRepository.search(criteria);

        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getTotalCount()).isEqualTo(4);
        assertThat(result.getItems().get(0).getUser()).isEqualTo(user1);

        //next search
        nextSearchAfter = result.getGetNextSearchAfter();
        criteria.setNextSearchAfter(nextSearchAfter);
        result = reviewRepository.search(criteria);

        assertThat(result.getItems()).isEmpty();
        assertThat(result.getTotalCount()).isEqualTo(4);
        assertThat(result.getGetNextSearchAfter()).isNull();
    }

    //user에 따라서

    //최신순
    @Test
    @DisplayName("reviewSearch - by user - recent")
    public void reviewSearch_recent_byUser() throws Exception {
        //given
        ReviewSearchCriteria criteria = ReviewSearchCriteria.builder()
                .size(1)
                .sortType(ReviewSortType.RECENT)
                .withTotalCount(true)
                .user(user1)
                .build();

        //when
        PageResult<Review> result = reviewRepository.search(criteria);

        //then
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getItem()).isEqualTo(item2);
        assertThat(result.getTotalCount()).isEqualTo(2);
        assertThat(result.getGetNextSearchAfter()).isNotNull();

        // search next

        String nextSearchAfter = result.getGetNextSearchAfter();
        criteria.setNextSearchAfter(nextSearchAfter);
        result = reviewRepository.search(criteria);

        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getItem()).isEqualTo(item1);
        assertThat(result.getTotalCount()).isEqualTo(2);
        assertThat(result.getGetNextSearchAfter()).isNotNull();

        // search next

        nextSearchAfter = result.getGetNextSearchAfter();
        criteria.setNextSearchAfter(nextSearchAfter);
        result = reviewRepository.search(criteria);

        assertThat(result.getItems()).isEmpty();
        assertThat(result.getTotalCount()).isEqualTo(2);
        assertThat(result.getGetNextSearchAfter()).isNull();
    }

    //아이템순
    @Test
    @DisplayName("reviewSearch - by user - item")
    public void reviewSearch_item_byUser() throws Exception {
        //given

        //when

        //then

    }


}