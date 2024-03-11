package com.website.repository.item.thumbnail;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;

@Slf4j
@RequiredArgsConstructor
public class ItemThumbnailCustomRepositoryImpl implements ItemThumbnailCustomRepository{

    private final EntityManager em;
    private final JPAQueryFactory query;

    //CRUD

    //Delete시에 해야할 것 기존에 있던 썸네일 삭제해야함.

}
