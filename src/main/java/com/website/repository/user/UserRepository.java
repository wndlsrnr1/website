package com.website.repository.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.user.User;
import org.springframework.stereotype.Repository;

import static com.website.domain.user.QUser.*;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class UserRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory jpaQueryFactory;
    private final UserJpaRepository userJpaRepository;

    public UserRepository(EntityManager entityManager, UserJpaRepository userJpaRepository) {
        this.entityManager = entityManager;
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
        this.userJpaRepository = userJpaRepository;
    }

    public User findNormalUserByEmailPassword(String emailParam, String passwordParam) {

        return jpaQueryFactory.select(user)
                .from(user)
                .where(user.email.eq(emailParam), user.password.eq(passwordParam))
                .fetchOne();
    }

    public User findByUserId(Long userId) {
        return userJpaRepository.findById(userId).get();
    }


    public void saveUser(User user) {
        userJpaRepository.save(user);
    }

    public User findUserByEmail(String email) {
        List<User> fetch = jpaQueryFactory.selectFrom(user).where(user.email.eq(email)).fetch();
        if (fetch.size() == 0) {
            return null;
        }
        return fetch.get(0);
    }


    //public User fail() {
    //    List select_noname_from_user = entityManager.createQuery("select name from user").getResultList();
    //    return null;
    //}
}
