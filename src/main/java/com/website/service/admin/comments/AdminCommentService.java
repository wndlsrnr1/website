package com.website.service.admin.comments;

import com.website.repository.comment.CommentRepository;
import com.website.service.comment.CommentValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCommentService {

    private final CommentRepository commentRepository;
    private final CommentValidator commentValidator;

    @Transactional
    public void removeComments(Long commentId) {

        commentValidator.validateCommentExists(commentId);

        commentRepository.deleteById(commentId);
    }
}
