package com.website.repository.purchases;

import com.website.repository.item.ItemRepository;
import com.website.repository.model.item.Item;
import com.website.repository.user.model.SocialType;
import com.website.repository.user.model.User;
import com.website.repository.purchases.model.OrderStatus;
import com.website.repository.purchases.model.Purchases;
import com.website.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("dev")
class PurchasesRepositoryCommitTest {

    @Autowired
    PurchasesRepository purchasesRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Test
    //@Commit
    void commit() {
        /*

         */

        List<User> user = List.of(userRepository.findByEmailAndSocialType("example3@naver.com", SocialType.NONE).get());
        Item item = itemRepository.findById(1213L).get();
        for (int i = 0; i < user.size(); i++) {
            Purchases purchases = Purchases.builder()
                    .orderNumber(UUID.randomUUID().toString())
                    .orderDate(LocalDateTime.now())
                    .user(user.get(i))
                    .item(item)
                    .status(OrderStatus.DELIVERED)
                    .totalAmount(1)
                    .address("지구")
                    .discount(10)
                    .notes("이상 없음")
                    .build();
            purchasesRepository.save(purchases);
        }
    }

}