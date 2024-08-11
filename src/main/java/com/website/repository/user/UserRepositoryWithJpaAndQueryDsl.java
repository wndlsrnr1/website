package com.website.repository.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.repository.model.user.User;
import org.springframework.stereotype.Repository;


import javax.persistence.EntityManager;
import java.util.List;

import static com.website.repository.model.user.QUser.user;

@Repository
public class UserRepositoryWithJpaAndQueryDsl implements UserRepository{

    private final EntityManager entityManager;
    private final JPAQueryFactory jpaQueryFactory;
    private final UserJpaRepository userJpaRepository;

    public UserRepositoryWithJpaAndQueryDsl(EntityManager entityManager, UserJpaRepository userJpaRepository) {
        this.entityManager = entityManager;
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User findNormalUserByEmailPassword(String emailParam, String passwordParam) {

        return jpaQueryFactory.select(user)
                .from(user)
                .where(user.email.eq(emailParam), user.password.eq(passwordParam))
                .fetchOne();
    }

    @Override
    public void saveUser(User user) {
        userJpaRepository.save(user);
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
        return userJpaRepository.findById(userId).orElse(null);
    }
}
