package com.website.common.repository.answer;

import com.website.common.repository.answer.model.CommentSearchWithAnswer;
import com.website.common.repository.answer.model.SearchCommentCriteria;
import com.website.common.repository.common.PageResult;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomAnswerRepository{
    PageResult<CommentSearchWithAnswer> searchComment(SearchCommentCriteria criteria);
}
