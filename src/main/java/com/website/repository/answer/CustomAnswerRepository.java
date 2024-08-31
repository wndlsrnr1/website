package com.website.repository.answer;

import com.website.repository.answer.model.CommentSearch;
import com.website.repository.answer.model.SearchCommentCriteria;
import com.website.repository.common.PageResult;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomAnswerRepository{
    PageResult<CommentSearch> searchComment(SearchCommentCriteria criteria);
}
