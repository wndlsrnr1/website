package com.website.repository.review;

import com.website.repository.common.PageResult;
import com.website.repository.review.model.Review;
import com.website.service.review.model.ReviewSearchCriteria;

public interface CustomReviewRepository {
    PageResult<Review> search(ReviewSearchCriteria criteria);
}
