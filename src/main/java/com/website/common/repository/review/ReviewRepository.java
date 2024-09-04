package com.website.common.repository.review;

import com.website.common.repository.purchases.model.Purchases;
import com.website.common.repository.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {

    Optional<Review> findByPurchases(Purchases purchases);

    boolean existsByPurchases(Purchases purchase);
}
