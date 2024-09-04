package com.website.common.repository.comment;

import com.website.common.repository.comment.model.Comment;
import com.website.common.repository.comment.model.CommentSearchCriteria;
import com.website.common.repository.comment.model.CommentWithAnswer;
import com.website.common.repository.common.PageResult;

public interface CustomCommentRepository {
    PageResult<Comment> search(CommentSearchCriteria criteria);

    PageResult<CommentWithAnswer> searchV2(CommentSearchCriteria criteria);
}
