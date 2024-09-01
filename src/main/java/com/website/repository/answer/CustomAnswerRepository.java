package com.website.repository.answer;

import com.website.repository.answer.model.CommentSearchWithAnswer;
import com.website.repository.answer.model.SearchCommentCriteria;
import com.website.repository.common.PageResult;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomAnswerRepository{
    PageResult<CommentSearchWithAnswer> searchComment(SearchCommentCriteria criteria);
}
