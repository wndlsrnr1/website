package com.website.repository.review;

import com.website.repository.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {
}
