package com.website.common.service.comment;

import com.website.common.exception.ClientException;
import com.website.common.exception.ErrorCode;
import com.website.common.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentValidator {

    private final CommentRepository commentRepository;

    public void validateCommentExists(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new ClientException(ErrorCode.BAD_REQUEST,
                    "comment not found. commentId = " + commentId);
        }
    }

}
