package com.website.service.comment;

import com.website.exception.ClientException;
import com.website.exception.ErrorCode;
import com.website.repository.model.item.Item;
import com.website.repository.user.model.User;
import com.website.repository.comment.CommentRepository;
import com.website.repository.comment.model.Comment;
import com.website.service.item.ItemValidator;
import com.website.service.comment.model.CommentCreateDto;
import com.website.service.comment.model.CommentDto;
import com.website.service.comment.model.CommentUpdateDto;
import com.website.service.user.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("local")
@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {CommentService.class})
class CommentServiceTest {

    @MockBean
    CommentRepository commentRepository;

    @MockBean
    UserValidator userValidator;

    @MockBean
    ItemValidator itemValidator;

    @Autowired
    CommentService commentService;

    private Long userId;
    private Long itemId;
    private Long commentId;
    private User user;
    private Item item;
    private Comment comment;
    private CommentCreateDto commentCreateDto;

    @BeforeEach
    void setUp() {
        userId = 1L;
        itemId = 1L;
        commentId = 1L;
        user = User.builder().id(userId).build();
        item = Item.builder().id(itemId).build();
        comment = Comment.builder()
                .user(user)
                .item(item)
                .build();

        commentCreateDto = CommentCreateDto.builder()
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
    @DisplayName("[Comment register] - success")
    public void registerCommentTest() throws Exception {
        //given
        when(userValidator.validateAndGet(userId)).thenReturn(user);
        when(itemValidator.validateAndGet(itemId)).thenReturn(item);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        //when
        CommentDto commentDto = commentService.registerComment(this.commentCreateDto);

        //then
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(itemValidator, times(1)).validateAndGet(itemId);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("[Comment register] - userId is null")
    public void registerCommentTest1() throws Exception {
        //given
        doThrow(new ClientException(ErrorCode.BAD_REQUEST, "userId is null"))
                .when(userValidator).validateAndGet(null);

        //when

        ClientException clientException = assertThrows(ClientException.class, () -> userValidator.validateAndGet(null));

        //then
        assertThat(clientException.getServerMessage()).contains("userId is null");
        verify(userValidator, times(1)).validateAndGet(null);
        verify(itemValidator, times(0)).validateAndGet(any(Long.class));
        verify(commentRepository, times(0)).save(any(Comment.class));
    }

    @Test
    @DisplayName("[Comment register] - user not found")
    public void registerCommentTest2() throws Exception {
        //given
        doThrow(new ClientException(ErrorCode.BAD_REQUEST, "user not found. userId=" + userId))
                .when(userValidator).validateAndGet(userId);

        //when
        ClientException exception = assertThrows(ClientException.class, () -> userValidator.validateAndGet(userId));
        assertThat(exception.getServerMessage()).contains("found. userId=" + userId);

        //then
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(itemValidator, times(0)).validateAndGet(any(Long.class));
        verify(commentRepository, times(0)).save(any(Comment.class));
    }

    @Test
    @DisplayName("[Comment register] - item is null")
    public void registerCommentTest3() throws Exception {
        // 이런 파라미터로 실행 하였을때 이것을 리턴 해라.
        // given
        when(userValidator.validateAndGet(userId)).thenReturn(user);
        doThrow(new ClientException(ErrorCode.BAD_REQUEST, "comment not found."))
                .when(itemValidator).validateAndGet(commentId);

        // 실행
        //when
        ClientException exception = assertThrows(ClientException.class, () -> commentService.removeComment(userId, commentId));
        assertThat(exception.getServerMessage()).contains("comment not found");

        // 검증
        //then
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(commentRepository, times(0)).save(any(Comment.class));
    }

    //when, thenReturn, doThrow, assertThrows, verify.
    @Test
    @DisplayName("[Comment register] - itemId not found")
    public void registerCommentTest4() throws Exception {
        //given
        when(userValidator.validateAndGet(userId)).thenReturn(null);
        doThrow(new ClientException(ErrorCode.BAD_REQUEST, "itemId not found"))
                .when(itemValidator).validateAndGet(itemId);

        //when
        ClientException exception = assertThrows(ClientException.class, () -> commentService.registerComment(commentCreateDto));

        //then
        assertThat(exception.getServerMessage()).contains("itemId not found");
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(itemValidator, times(1)).validateAndGet(itemId);
        verify(commentRepository, times(0)).save(any(Comment.class));
    }

    @Test
    @DisplayName("[Comment find] - user not found")
    public void getCommentByUserIdAndItemIdTestV1() throws Exception {
        //given
        doThrow(new ClientException(ErrorCode.BAD_REQUEST, "user not found"))
                .when(userValidator).validateAndGet(userId);

        //when
        ClientException exception = assertThrows(ClientException.class, () ->
                commentService.getCommentByUserIdAndItemId(userId, itemId));

        //then
        assertThat(exception.getMessage()).contains("user not");
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(itemValidator, times(0)).validateAndGet(any(Long.class));
        verify(commentRepository, times(0)).findByUserAndItem(any(User.class), any(Item.class));
    }

    @Test
    @DisplayName("[Comment find] - item not found")
    public void getCommentByUserIdAndItemIdTestV2() throws Exception {
        //given
        when(userValidator.validateAndGet(userId)).thenReturn(user);
        doThrow(new ClientException(ErrorCode.BAD_REQUEST, "user not found"))
                .when(itemValidator).validateAndGet(itemId);

        //when
        ClientException exception = assertThrows(ClientException.class, () ->
                commentService.getCommentByUserIdAndItemId(userId, itemId));

        //then
        assertThat(exception.getMessage()).contains("user not");
        verify(userValidator, times(1)).validateAndGet(any(Long.class));
        verify(itemValidator, times(1)).validateAndGet(itemId);
        verify(commentRepository, times(0)).findByUserAndItem(any(User.class), any(Item.class));
    }

    @Test
    @DisplayName("[Comment find] - comment not found")
    public void getCommentByUserIdAndItemIdTestV3() throws Exception {
        //given
        when(userValidator.validateAndGet(userId)).thenReturn(user);
        when(itemValidator.validateAndGet(itemId)).thenReturn(item);
        when(commentRepository.findByUserAndItem(user, item)).thenReturn(List.of());

        //when
        commentService.getCommentByUserIdAndItemId(userId, itemId);

        //then
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(itemValidator, times(1)).validateAndGet(itemId);
        verify(commentRepository, times(1)).findByUserAndItem(user, item);
    }

    @Test
    @DisplayName("[Comment find] - success")
    public void getCommentByUserIdAndItemIdTestV4() throws Exception {
        //given
        when(userValidator.validateAndGet(userId)).thenReturn(user);
        when(itemValidator.validateAndGet(itemId)).thenReturn(item);
        when(commentRepository.findByUserAndItem(user, item)).thenReturn(List.of(comment));

        //when
        List<CommentDto> commentDto = commentService.getCommentByUserIdAndItemId(userId, itemId);

        //then
        assertThat(commentDto.get(0).getUsername()).isEqualTo(comment.getUser().getName());
        assertThat(commentDto.get(0).getItemId()).isEqualTo(comment.getItem().getId());
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(itemValidator, times(1)).validateAndGet(itemId);
        verify(commentRepository, times(1)).findByUserAndItem(user, item);
    }

    //update
    @Test
    @DisplayName("[Comment update] - success")
    public void updateCommentTestV1() throws Exception {
        user.setId(userId);
        item.setId(itemId);

        CommentUpdateDto updateDto = CommentUpdateDto.builder()
                .userId(userId)
                .commentId(commentId)
                .content("updatedContent")
                .star(4)
                .build();

        Comment beforeUpdate = Comment.builder()
                .id(commentId)
                .item(item)
                .user(user)
                .build();

        Comment afterUpdate = Comment.builder()
                .id(commentId)
                .item(item)
                .user(user)
                .content(updateDto.getContent())
                .star(updateDto.getStar())
                .build();

        when(userValidator.validateAndGet(userId)).thenReturn(user);
        when(commentRepository.findById(updateDto.getCommentId())).thenReturn(Optional.of(beforeUpdate));
        when(commentRepository.save(beforeUpdate)).thenReturn(afterUpdate);
        CommentDto commentDto = commentService.updateComment(updateDto);

        assertThat(commentDto.getId()).isEqualTo(commentId);
        assertThat(commentDto.getContent()).isEqualTo(updateDto.getContent());
        assertThat(commentDto.getStar()).isEqualTo(updateDto.getStar());

        verify(userValidator, times(1)).validateAndGet(userId);
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).save(beforeUpdate);
    }

    @Test
    @DisplayName("[Comment update] - comment not found")
    public void updateCommentTestV2() throws Exception {
        // Given
        when(userValidator.validateAndGet(userId)).thenReturn(user);

        CommentUpdateDto updateDto = CommentUpdateDto.builder()
                .commentId(commentId)
                .commentId(commentId)
                .userId(userId)
                .content("updatedContent")
                .star(4)
                .build();

        // When
        ClientException exception = assertThrows(ClientException.class,
                () -> commentService.updateComment(updateDto)
        );

        // Then
        assertThat(exception.getServerMessage()).contains("comment not found");
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(0)).save(any(Comment.class));
    }


    //delete
    @Test
    @DisplayName("[Comment remove] - success")
    public void removeCommentTestV1() throws Exception {
        //given
        when(userValidator.validateAndGet(userId)).thenReturn(user);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        //when
        commentService.removeComment(userId, commentId);

        //then
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    @DisplayName("[Comment remove] - user validate fail")
    public void removeCommentTestV2() throws Exception {
        //given
        doThrow(new ClientException(ErrorCode.BAD_REQUEST, "some error in user validation"))
                .when(userValidator).validateAndGet(userId);

        //when
        ClientException ex = assertThrows(ClientException.class, () ->
                commentService.removeComment(userId, itemId));

        //then
        assertThat(ex.getServerMessage()).contains("error in user");
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(itemValidator, times(0)).validateAndGet(any(Long.class));
        verify(commentRepository, times(0)).findByUserAndItem(any(User.class), any(Item.class));
        verify(commentRepository, times(0)).delete(any(Comment.class));
    }

    @Test
    @DisplayName("[Comment remove] - comment not found")
    public void removeCommentTestV4() throws Exception {
        //given
        when(userValidator.validateAndGet(userId)).thenReturn(user);
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        //when
        ClientException ex = assertThrows(ClientException.class, () ->
                commentService.removeComment(userId, commentId));

        //then
        assertThat(ex.getServerMessage()).contains("comment not found. userId =");
        verify(userValidator, times(1)).validateAndGet(userId);
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(0)).delete(any(Comment.class));
    }

}