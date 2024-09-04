package com.website.repository.purchases;

import com.website.common.repository.purchases.CustomPurchasesRepositoryImpl;
import com.website.config.jpa.JpaConfig;
import com.website.common.repository.comment.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import({CustomPurchasesRepositoryImpl.class, JpaConfig.class})
@ActiveProfiles("local")
class PurchasesRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Test
    void commit() {

    }

}