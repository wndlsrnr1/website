package com.website.repository.review;

import com.website.common.repository.purchases.PurchasesRepository;
import com.website.common.repository.purchases.model.Purchases;
import com.website.common.repository.review.ReviewRepository;
import com.website.common.repository.review.model.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("dev")
public class ReviewInsertData {

    @Autowired
    PurchasesRepository purchasesRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Test
    //@Commit
    public void commitReviewData() {
        List<Purchases> purchasesList = purchasesRepository.findAll();
        for (int i = 0; i < purchasesList.size(); i++) {
            Purchases purchases = purchasesList.get(i);
            reviewRepository.save(
                    Review.builder()
                            .purchases(purchases)
                            .content("content " + i)
                            .star((i % 5) + 1)
                            .build()
            );
        }
    }

}
