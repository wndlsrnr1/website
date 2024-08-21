package com.website.repository.comment;

import com.website.config.jpa.JpaConfig;
import com.website.repository.item.ItemRepository;
import com.website.repository.model.item.Item;
import com.website.repository.comment.model.Comment;
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
@Import({CustomCommentRepositoryImpl.class, JpaConfig.class})
@ActiveProfiles("local")
class CommentJpaRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CommentRepository commentRepository;
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
    public void commentRepository_create() throws Exception {
        //given
        Comment comment = Comment.builder()
                .item(item)
                .user(user)
                .star(4)
                .content("test")
                .build();

        //when
        Comment savedComment = commentRepository.save(comment);

        //then
        assertThat(savedComment.getId()).isNotNull();
        assertThat(savedComment.getContent()).isEqualTo("test");
        assertThat(savedComment.getItem().getName()).isEqualTo("name");
        assertThat(savedComment.getUser().getName()).isEqualTo("test");
        assertThat(savedComment.getStar()).isEqualTo(4);
        assertThat(savedComment.getCreatedAt()).isNotNull();
        assertThat(savedComment.getUpdatedAt()).isNotNull();
    }

    // read

    @Test
    @DisplayName("Read Test - findByUser")
    public void commentRepository_findByUser() throws Exception {
        //given
        Comment comment = Comment.builder()
                .item(item)
                .user(user)
                .star(4)
                .content("test")
                .build();
        Comment savedComment = commentRepository.save(comment);

        //when
        List<Comment> findComment = commentRepository.findByUser(user);

        //then
        assertThat(findComment).hasSize(1);
        assertThat(findComment.get(0).getUser().getName()).isEqualTo("test");
        assertThat(findComment.get(0).getItem().getName()).isEqualTo("name");
        assertThat(findComment.get(0).getStar()).isEqualTo(4);
    }

    @Test
    @DisplayName("Read Test - findByProduct")
    public void commentRepository_findByProduct() throws Exception {
        //given
        Comment comment = Comment.builder()
                .item(item)
                .user(user)
                .star(4)
                .content("test")
                .build();
        Comment savedComment = commentRepository.save(comment);

        //when
        List<Comment> findComment = commentRepository.findByItem(item);

        //then
        assertThat(findComment).hasSize(1);
        assertThat(findComment.get(0).getUser().getName()).isEqualTo("test");
        assertThat(findComment.get(0).getItem().getName()).isEqualTo("name");
        assertThat(findComment.get(0).getStar()).isEqualTo(4);
    }

    @Test
    @DisplayName("Read Test - findByUserAndItem")
    public void commentRepository_findByUserAndItem() throws Exception {
        //given
        Comment comment = Comment.builder()
                .item(item)
                .user(user)
                .star(4)
                .content("test")
                .build();
        Comment savedComment = commentRepository.save(comment);

        //when
        List<Comment> findComment = commentRepository.findByUserAndItem(user, item);

        //then
        assertThat(findComment).isNotEmpty();
        assertThat(findComment.get(0).getUser().getId()).isEqualTo(user.getId());
        assertThat(findComment.get(0).getItem().getId()).isEqualTo(item.getId());
        assertThat(findComment.get(0).getStar()).isEqualTo(4);
    }

    // update
    @Test
    @DisplayName("Update Test - update content and star")
    public void commentRepository_updateComment() throws Exception {
        //given
        Comment comment = Comment.builder()
                .item(item)
                .user(user)
                .star(4)
                .content("test")
                .build();

        Comment savedComment = commentRepository.save(comment);

        //when
        savedComment.setContent("test2");
        savedComment.setStar(5);
        commentRepository.save(savedComment);

        Long commentId = savedComment.getId();

        //then
        Comment findComment = commentRepository.findById(commentId).get();
        assertThat(findComment.getId()).isEqualTo(savedComment.getId());
        assertThat(findComment.getUser().getId()).isEqualTo(user.getId());
        assertThat(findComment.getItem().getId()).isEqualTo(item.getId());
        assertThat(findComment.getStar()).isEqualTo(5);
        assertThat(findComment.getContent()).isEqualTo("test2");

    }

    // delete
    @Test
    @DisplayName("Delete Test - byId")
    void commentRepository_deleteComment() {
        //given
        Comment comment = Comment.builder()
                .item(item)
                .user(user)
                .star(4)
                .content("test")
                .build();
        Comment savedComment = commentRepository.save(comment);
        Long commentId = savedComment.getId();

        //when
        commentRepository.deleteById(savedComment.getId());


        Optional<Comment> findComment = commentRepository.findById(commentId);
        assertThat(findComment).isNotPresent();
    }

}