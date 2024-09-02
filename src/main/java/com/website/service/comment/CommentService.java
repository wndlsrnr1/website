package com.website.service.comment;

import com.website.exception.ClientException;
import com.website.exception.ErrorCode;
import com.website.repository.common.PageResult;
import com.website.repository.model.item.Item;
import com.website.repository.user.model.User;
import com.website.repository.comment.CommentRepository;
import com.website.repository.comment.model.Comment;
import com.website.repository.comment.model.CommentSearchCriteria;
import com.website.service.common.model.PageResultDto;
import com.website.service.item.ItemValidator;
import com.website.service.comment.model.*;
import com.website.service.user.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserValidator userValidator;
    private final ItemValidator itemValidator;

    // create
    @Transactional
    public CommentDto registerComment(CommentCreateDto dto) {
        User user = userValidator.validateAndGet(dto.getUserId());
        Item item = itemValidator.validateAndGet(dto.getItemId());

        Comment comment = Comment.builder()
                .item(item)
                .user(user)
                .content(dto.getContent())
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentDto.of(savedComment);
    }

    // read
    @Transactional(readOnly = true)
    public CommentDto getCommentById(Long commentId) {
        if (commentId == null) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "commentId is null");
        }

        Comment foundComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ClientException(ErrorCode.BAD_REQUEST, "comment not found. commentId = " + commentId));

        return CommentDto.of(foundComment);
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getCommentByUserIdAndItemId(Long userId, Long itemId) {
        User user = userValidator.validateAndGet(userId);
        Item item = itemValidator.validateAndGet(itemId);

        List<Comment> foundComment = commentRepository.findByUserAndItem(user, item);
        return foundComment.stream().map(CommentDto::of).collect(Collectors.toList());
    }

    // update
    @Transactional
    public CommentDto updateComment(CommentUpdateDto dto) {
        User user = userValidator.validateAndGet(dto.getUserId());
        //Item item = itemValidator.validateAndGet(dto.getItemId());

        Comment comment = commentRepository.findById(dto.getCommentId()).orElseThrow(
                () -> new ClientException(ErrorCode.BAD_REQUEST, "comment not found commentId = " + user.getId() + ", commentId = " + dto.getCommentId())
        );

        comment.setContent(dto.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return CommentDto.of(updatedComment);
    }

    //delete
    @Transactional
    public void removeComment(Long userId, Long commentId) {
        User user = userValidator.validateAndGet(userId);
        //Item item = itemValidator.validateAndGet(commentId);

        Comment foundComment = commentRepository.findById(commentId).orElseThrow(() ->
                new ClientException(ErrorCode.BAD_REQUEST, "comment not found. userId = " + userId + ", commentId = " + commentId));

        commentRepository.delete(foundComment);
    }

    @Transactional(readOnly = true)
    public PageResultDto<CommentDto> searchComment(CommentSearchRequestDto commentSearchRequestDto) {

        CommentSearchCriteria criteria = commentSearchRequestDto.toCriteria();
        PageResult<Comment> findResult = commentRepository.search(criteria);

        return PageResultDto.<CommentDto>builder()
                .items(findResult.getItems().stream().map(CommentDto::of).collect(Collectors.toList()))
                .totalCount(findResult.getTotalCount())
                .nextSearchAfter(findResult.getNextSearchAfter())
                .build();
    }


}
