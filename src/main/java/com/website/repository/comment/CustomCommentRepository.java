package com.website.repository.comment;

import com.website.repository.common.PageResult;
import com.website.repository.comment.model.Comment;
import com.website.repository.comment.model.CommentSearchCriteria;

public interface CustomCommentRepository {
    PageResult<Comment> search(CommentSearchCriteria criteria);
}
