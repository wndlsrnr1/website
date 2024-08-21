package com.website.repository.purchases;

import com.website.config.jpa.JpaConfig;
import com.website.repository.review.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({CustomPurchasesRepositoryImpl.class, JpaConfig.class})
@ActiveProfiles("local")
class PurchasesRepositoryTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Test
    void commit() {

    }

}