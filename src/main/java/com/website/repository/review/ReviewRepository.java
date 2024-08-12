package com.website.repository.review;

import com.website.repository.model.item.Item;
import com.website.repository.review.model.Review;
import com.website.repository.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository{

    boolean existsByItem(Item item);

    boolean existsByUser(User user);

    boolean existsById(Long id);

    Optional<Review> findById(Long id);

    Optional<Review> findByUserAndItem(User user, Item item);

    List<Review> findByItem(Item item);

    List<Review> findByUser(User user);

}
