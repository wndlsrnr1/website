package com.website.repository.comment;

import com.website.common.repository.comment.CommentRepository;
import com.website.common.repository.comment.CustomCommentRepositoryImpl;
import com.website.config.jpa.JpaConfig;
import com.website.common.repository.common.PageResult;
import com.website.common.repository.item.ItemRepository;
import com.website.common.repository.model.item.Item;
import com.website.common.repository.user.model.User;
import com.website.common.repository.comment.model.Comment;
import com.website.common.repository.comment.model.CommentSearchCriteria;
import com.website.common.repository.comment.model.CommentSortType;
import com.website.common.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfig.class, CustomCommentRepositoryImpl.class})
@ActiveProfiles("local")
public class CommentJpaRepositorySearchTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

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

        //comment 저장
        //commentRepository.save(Comment.builder().item(item1).user(user1).content("content1").star(1).build());
        //commentRepository.save(Comment.builder().item(item1).user(user2).content("content2").star(1).build());
        //commentRepository.save(Comment.builder().item(item1).user(user3).content("content3").star(2).build());
        //commentRepository.save(Comment.builder().item(item1).user(user4).content("content4").star(2).build());
        //
        //commentRepository.save(Comment.builder().item(item2).user(user4).content("content5").star(4).build());
        //commentRepository.save(Comment.builder().item(item2).user(user3).content("content6").star(4).build());
        //commentRepository.save(Comment.builder().item(item2).user(user2).content("content7").star(3).build());
        //commentRepository.save(Comment.builder().item(item2).user(user1).content("content8").star(3).build());
    }

    //item에 따라서

    //최신순
    @Test
    @DisplayName("commentSearch - by Item - recent")
    public void commentSearch_recent_byItem() throws Exception {
        //given
        CommentSearchCriteria criteria = CommentSearchCriteria.builder()
                .size(2)
                .sortType(CommentSortType.RECENT)
                .withTotalCount(true)
                .itemId(item1.getId())
                .build();

        // when
        PageResult<Comment> result = commentRepository.search(criteria);

        // then
        assertThat(result.getTotalCount()).isEqualTo(4);
        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getItems().get(0).getUser()).isEqualTo(user4);
        assertThat(result.getNextSearchAfter()).isNotNull();

        // search next page
        String nextSearchAfter = result.getNextSearchAfter();
        criteria.setNextSearchAfter(nextSearchAfter);
        result = commentRepository.search(criteria);

        assertThat(result.getTotalCount()).isEqualTo(4);
        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getItems().get(0).getUser()).isEqualTo(user2);
        assertThat(result.getNextSearchAfter()).isNotNull();

        // search next page
        nextSearchAfter = result.getNextSearchAfter();
        criteria.setNextSearchAfter(nextSearchAfter);
        result = commentRepository.search(criteria);

        assertThat(result.getTotalCount()).isEqualTo(4);
        assertThat(result.getItems()).isEmpty();
        assertThat(result.getNextSearchAfter()).isNull();
    }

    //별점순
    @Test
    @DisplayName("commentSearch - by item - star")
    public void commentSearch_star_byItem() throws Exception {
        //given
        CommentSearchCriteria criteria = CommentSearchCriteria.builder()
                .size(2)
                //.sortType(CommentSortType.STAR)
                .withTotalCount(true)
                .itemId(item2.getId())
                .build();

        //when
        PageResult<Comment> result = commentRepository.search(criteria);

        //then
        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getTotalCount()).isEqualTo(4);
        assertThat(result.getItems().get(0).getUser()).isEqualTo(user3);
        assertThat(result.getNextSearchAfter()).isNotNull();


        // next search

        String nextSearchAfter = result.getNextSearchAfter();
        criteria.setNextSearchAfter(nextSearchAfter);
        result = commentRepository.search(criteria);

        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getTotalCount()).isEqualTo(4);
        assertThat(result.getItems().get(0).getUser()).isEqualTo(user1);

        //next search
        nextSearchAfter = result.getNextSearchAfter();
        criteria.setNextSearchAfter(nextSearchAfter);
        result = commentRepository.search(criteria);

        assertThat(result.getItems()).isEmpty();
        assertThat(result.getTotalCount()).isEqualTo(4);
        assertThat(result.getNextSearchAfter()).isNull();
    }

    //user에 따라서

    //최신순
    @Test
    @DisplayName("commentSearch - by user - recent")
    public void commentSearch_recent_byUser() throws Exception {
        //given
        CommentSearchCriteria criteria = CommentSearchCriteria.builder()
                .size(1)
                .sortType(CommentSortType.RECENT)
                .withTotalCount(true)
                .userId(user1.getId())
                .build();

        //when
        PageResult<Comment> result = commentRepository.search(criteria);

        //then
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getItem()).isEqualTo(item2);
        assertThat(result.getTotalCount()).isEqualTo(2);
        assertThat(result.getNextSearchAfter()).isNotNull();

        // search next

        String nextSearchAfter = result.getNextSearchAfter();
        criteria.setNextSearchAfter(nextSearchAfter);
        result = commentRepository.search(criteria);

        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getItem()).isEqualTo(item1);
        assertThat(result.getTotalCount()).isEqualTo(2);
        assertThat(result.getNextSearchAfter()).isNotNull();

        // search next

        nextSearchAfter = result.getNextSearchAfter();
        criteria.setNextSearchAfter(nextSearchAfter);
        result = commentRepository.search(criteria);

        assertThat(result.getItems()).isEmpty();
        assertThat(result.getTotalCount()).isEqualTo(2);
        assertThat(result.getNextSearchAfter()).isNull();
    }

    //아이템순
    @Test
    @DisplayName("commentSearch - by user - item")
    public void commentSearch_item_byUser() throws Exception {
        //given

        //when

        //then

    }


}
