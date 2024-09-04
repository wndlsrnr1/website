package com.website.common.repository.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.common.repository.user.model.User;
import org.springframework.stereotype.Repository;


import javax.persistence.EntityManager;
import java.util.List;

import static com.website.common.repository.user.model.QUser.user;

@Repository
public class CustomUserRepositoryWithJpaAndQueryDsl implements CustomUserRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory jpaQueryFactory;
    private final UserRepository userRepository;

    public CustomUserRepositoryWithJpaAndQueryDsl(EntityManager entityManager, UserRepository userRepository) {
        this.entityManager = entityManager;
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
    @Override
    public User findUserByEmail(String email) {
        List<User> fetch = jpaQueryFactory.selectFrom(user).where(user.email.eq(email)).fetch();
        if (fetch.size() == 0) {
            return null;
        }
        return fetch.get(0);
    }


    @Override
    public User findByUserId(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
