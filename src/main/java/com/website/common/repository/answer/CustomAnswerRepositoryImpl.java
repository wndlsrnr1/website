package com.website.common.repository.answer;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.common.exception.ClientException;
import com.website.common.exception.ErrorCode;
import com.website.common.repository.answer.model.CommentSearchWithAnswer;
import com.website.common.repository.answer.model.SearchCommentCriteria;
import com.website.common.repository.comment.model.CommentSortType;
import com.website.common.repository.answer.model.QAnswer;
import com.website.common.repository.answer.model.QCommentSearchWithAnswer;
import com.website.common.repository.comment.model.QComment;
import com.website.common.repository.common.PageResult;
import com.website.common.repository.model.category.QCategory;
import com.website.common.repository.model.category.QSubcategory;
import com.website.common.repository.model.item.QItemSubcategory;
import com.website.utils.common.SearchAfterEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.Arrays;
import java.util.List;

import static com.website.common.repository.answer.model.QAnswer.answer;
import static com.website.common.repository.comment.model.QComment.comment;
import static com.website.common.repository.model.category.QCategory.category;
import static com.website.common.repository.model.category.QSubcategory.subcategory;
import static com.website.common.repository.model.item.QItemSubcategory.itemSubcategory;

@Repository
@Slf4j
public class CustomAnswerRepositoryImpl implements CustomAnswerRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory query;

    public CustomAnswerRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.query = new JPAQueryFactory(entityManager);
    }


    @Override
    public PageResult<CommentSearchWithAnswer> searchComment(SearchCommentCriteria criteria) {
        List<CommentSearchWithAnswer> result = query.select(new QCommentSearchWithAnswer(
                        comment.id,
                        comment.content,
                        comment.createdAt,
                        comment.updatedAt,
                        comment.item.id,
                        comment.user.id,
                        answer.id
                ))
                .from(comment)
                .where(where(whereByParams(criteria), whereByStatefulParams(criteria)))
                .join(itemSubcategory).on(itemSubcategory.item.id.eq(comment.item.id))
                .join(subcategory).on(subcategory.id.eq(itemSubcategory.subcategory.id))
                .join(category).on(subcategory.category.id.eq(category.id))
                .leftJoin(answer).on(answer.commentId.eq(comment.id))
                .limit(criteria.getSize())
                .orderBy(order(criteria.getSortType()))
                .fetch();

        Long totalCount = getTotalCount(criteria);

        String nextSearchAfter = getNextSearchAfter(result, criteria);

        return PageResult.<CommentSearchWithAnswer>builder()
                .items(result)
                .nextSearchAfter(nextSearchAfter)
                .totalCount(totalCount)
                .build();
    }

    private String getNextSearchAfter(List<CommentSearchWithAnswer> result, SearchCommentCriteria criteria) {
        String nextSearchAfter = null;
        if (criteria.getSize() == result.size()) {
            CommentSearchWithAnswer lastElement = result.get(result.size() - 1);
            log.info("last lastElement = {}", lastElement);
            switch (criteria.getSortType()) {
                case RECENT:
                    nextSearchAfter = SearchAfterEncoder.encode(lastElement.getCommentId().toString());
                    break;
                case ITEM:
                    break;
                default: {
                    throw new ClientException(ErrorCode.BAD_REQUEST, "unimplemented sort type. sorType = " + criteria.getSortType());
                }
            }
        }
        return nextSearchAfter;
    }

    private Long getTotalCount(SearchCommentCriteria criteria) {
        if (!criteria.isWithTotalCount()) {
            return null;
        }
        return query.select(new QCommentSearchWithAnswer(
                        comment.id,
                        comment.content,
                        comment.createdAt,
                        comment.updatedAt,
                        comment.item.id,
                        comment.user.id,
                        answer.id
                ))
                .from(comment)
                .where(where(whereByParams(criteria), whereByStatefulParams(criteria)))
                .join(itemSubcategory).on(itemSubcategory.item.id.eq(comment.item.id))
                .join(subcategory).on(subcategory.id.eq(itemSubcategory.subcategory.id))
                .join(category).on(subcategory.category.id.eq(category.id))
                .leftJoin(answer).on(answer.commentId.eq(comment.id))
                .fetchCount();
    }

    private BooleanExpression[] where(BooleanExpression[] whereByParams, BooleanExpression whereByStatefulParams) {
        if (whereByStatefulParams == null) {
            return whereByParams;
        }
        BooleanExpression[] newArray = Arrays.copyOf(whereByParams, whereByParams.length + 1);
        newArray[newArray.length - 1] = whereByStatefulParams;
        return newArray;
    }

    private BooleanExpression[] whereByParams(SearchCommentCriteria criteria) {
        return new BooleanExpression[]{
                categoryEq(criteria.getCategoryId()),
                subcategoryEq(criteria.getSubcategoryId()),
                userIdEq(criteria.getUserId()),
                itemIdEq(criteria.getItemId()),
                isAnswer(criteria.isNoneWithAnswer())
        };
    }

    private BooleanExpression whereByStatefulParams(SearchCommentCriteria criteria) {
        if (criteria.getNextSearchAfter() == null) {
            return null;
        }

        switch (criteria.getSortType()) {
            case RECENT: {
                String decoded = SearchAfterEncoder.decodeSingle(criteria.getNextSearchAfter());
                Long id = Long.valueOf(decoded);
                return comment.id.lt(id);
            }
            default: {
                throw new ClientException(ErrorCode.BAD_REQUEST, "unimplemented sort type. sorType = " + criteria.getSortType());
            }
        }
    }

    private OrderSpecifier<?>[] order(CommentSortType sortType) {
        switch (sortType) {
            case RECENT: {
                return new OrderSpecifier[]{
                        comment.id.desc()
                };
            }
            default: {
                throw new ClientException(ErrorCode.BAD_REQUEST, "unimplemented sort type. sorType = " + sortType);
            }
        }
    }

    private BooleanExpression categoryEq(Long categoryId) {
        return categoryId != null ? category.id.eq(categoryId) : null;
    }

    private BooleanExpression subcategoryEq(Long subcategoryId) {
        return subcategoryId != null ? itemSubcategory.subcategory.id.eq(subcategoryId) : null;
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? comment.user.id.eq(userId) : null;
    }

    private BooleanExpression itemIdEq(Long itemId) {
        return itemId != null ? comment.item.id.eq(itemId) : null;
    }

    private BooleanExpression isAnswer(boolean noneWithAnswer) {
        return noneWithAnswer ? comment.id.notIn(
                query.select(answer.commentId).from(answer)
        ) : null;
    }
}
