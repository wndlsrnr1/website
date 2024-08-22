package com.website.repository.review;

import com.website.repository.purchases.model.Purchases;
import com.website.repository.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {

    Optional<Review> findByPurchases(Purchases purchases);

    boolean existsByPurchases(Purchases purchase);
}
