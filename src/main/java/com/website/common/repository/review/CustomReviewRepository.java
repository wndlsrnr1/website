package com.website.common.repository.review;

import com.website.common.repository.common.PageResult;
import com.website.common.repository.review.model.Review;
import com.website.customer.service.review.model.ReviewSearchCriteria;

public interface CustomReviewRepository {
    PageResult<Review> search(ReviewSearchCriteria criteria);
}
