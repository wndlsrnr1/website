package com.website.repository.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.user.User;
import org.springframework.stereotype.Repository;

import static com.website.domain.user.QUser.*;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class UserJpaRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory jpaQueryFactory;
    private final UserRepository userRepository;

    public UserJpaRepository(EntityManager entityManager, UserRepository userRepository) {
        this.entityManager = entityManager;
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
        this.userRepository = userRepository;
    }

    public User findNormalUserByEmailPassword(String emailParam, String passwordParam) {
        return jpaQueryFactory.select(user)
                .from(user)
                .where(user.email.eq(emailParam), user.password.eq(passwordParam))
                .fetchOne();
    }


    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        List<User> fetch = jpaQueryFactory.selectFrom(user).where(user.email.eq(email)).fetch();
        if (fetch.size() == 0) {
            return null;
        }
        return fetch.get(0);
    }
}
