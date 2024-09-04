package com.website.repository.purchases;

import com.website.common.repository.item.ItemRepository;
import com.website.common.repository.model.item.Item;
import com.website.common.repository.purchases.PurchasesRepository;
import com.website.common.repository.user.model.User;
import com.website.common.repository.purchases.model.OrderStatus;
import com.website.common.repository.purchases.model.Purchases;
import com.website.common.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest
//@ActiveProfiles("dev")
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


        List<User> user = userRepository.findAll();
        List<Item> itemList = itemRepository.findAll();
        for (int i = 0; i < 100; i++) {

            Purchases purchases = Purchases.builder()
                    .orderNumber(UUID.randomUUID().toString())
                    .orderDate(LocalDateTime.now())
                    .user(user.get(i % user.size()))
                    .item(itemList.get((int)(Math.random() * itemList.size())))
                    .status(OrderStatus.DELIVERED)
                    .totalAmount(1)
                    .address("지구" + i)
                    .discount((int)(Math.random() * 10))
                    .notes("이상 없음")
                    .build();
            purchasesRepository.save(purchases);
        }
    }

}