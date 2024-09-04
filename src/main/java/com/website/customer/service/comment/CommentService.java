package com.website.customer.service.comment;

import com.website.common.exception.ClientException;
import com.website.common.exception.ErrorCode;
import com.website.common.repository.comment.model.CommentWithAnswer;
import com.website.common.repository.common.PageResult;
import com.website.common.repository.model.item.Item;
import com.website.common.repository.user.model.User;
import com.website.common.repository.comment.CommentRepository;
import com.website.common.repository.comment.model.Comment;
import com.website.common.repository.comment.model.CommentSearchCriteria;
import com.website.common.service.model.PageResultDto;
import com.website.common.service.item.ItemValidator;
import com.website.customer.service.user.UserValidator;
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
    public CommentDto updateComment(CommentUpdateDto dto, Long commentId) {
        User user = userValidator.validateAndGet(dto.getUserId());

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ClientException(ErrorCode.BAD_REQUEST, "comment not found commentId = " + user.getId() + ", commentId = " + commentId)
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


    public PageResultDto<CommentWithAnswerDto> searchCommentV2(CommentSearchRequestDto dto) {
        CommentSearchCriteria criteria = dto.toCriteria();
        PageResult<CommentWithAnswer> findResult = commentRepository.searchV2(criteria);
        return PageResultDto.<CommentWithAnswerDto>builder()
                .items(findResult.getItems().stream().map(CommentWithAnswerDto::of).collect(Collectors.toList()))
                .totalCount(findResult.getTotalCount())
                .nextSearchAfter(findResult.getNextSearchAfter())
                .build();
    }
}
