package com.website.repository.review;


import com.website.repository.model.item.review.QReview;
import com.website.repository.model.item.review.Review;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class CustomReviewRepositoryImpl extends QuerydslRepositorySupport implements CustomReviewRepository{

    private final QReview review = QReview.review;

    public CustomReviewRepositoryImpl() {
        super(Review.class);
    }



}
