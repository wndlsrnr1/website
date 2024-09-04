package com.website.common.repository.comment;


import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.common.exception.ClientException;
import com.website.common.exception.ErrorCode;
import com.website.common.repository.comment.model.Comment;
import com.website.common.repository.comment.model.CommentSearchCriteria;
import com.website.common.repository.comment.model.CommentSortType;
import com.website.common.repository.comment.model.CommentWithAnswer;
import com.website.common.repository.common.PageResult;
import com.website.common.repository.answer.model.QAnswer;
import com.website.common.repository.comment.model.*;
import com.website.utils.common.SearchAfterEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static com.website.common.repository.answer.model.QAnswer.answer;
import static com.website.common.repository.comment.model.QComment.comment;

@Slf4j
@Repository
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public CustomCommentRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public PageResult<Comment> search(CommentSearchCriteria criteria) {
        List<Comment> result = query.selectFrom(comment)
                .where(userIdEq(criteria.getUserId()), itemIdEq(criteria.getItemId()),
                        nextSearchProduct(criteria))
                .limit(criteria.getSize())
                .orderBy(orderBySort(criteria))
                .fetch();

        Long totalCount = getTotalCount(criteria);

        String nextSearchAfter = getNextSearchAfter(result, criteria);

        return PageResult.<Comment>builder()
                .items(result)
                .nextSearchAfter(nextSearchAfter)
                .totalCount(totalCount)
                .build();
    }

    @Override
    public PageResult<CommentWithAnswer> searchV2(CommentSearchCriteria criteria) {
        List<CommentWithAnswer> result = query.select(new QCommentWithAnswer(
                        comment.id,
                        comment.item.id,
                        comment.content,
                        comment.createdAt,
                        comment.updatedAt,
                        answer.id,
                        answer.content
                ))
                .from(comment)
                .where(where(whereByParams(criteria), whereByStatefulParams(criteria)))
                .leftJoin(answer).on(answer.commentId.eq(comment.id))
                .limit(criteria.getSize())
                .orderBy(order(criteria.getSortType()))
                .fetch();

        Long totalCount = getTotalCountForSearchV2(criteria);

        String nextSearchAfter = getNextSearchAfterV2(result, criteria);

        return PageResult.<CommentWithAnswer>builder()
                .items(result)
                .nextSearchAfter(nextSearchAfter)
                .totalCount(totalCount)
                .build();
    }

    private String getNextSearchAfterV2(List<CommentWithAnswer> result, CommentSearchCriteria criteria) {
        String nextSearchAfter =  null;
        if (criteria.getSize() == result.size()) {
            CommentWithAnswer lastElem = result.get(result.size() - 1);
            switch (criteria.getSortType()) {
                case RECENT:
                    nextSearchAfter = SearchAfterEncoder.encode(lastElem.getId().toString());
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

    private Long getTotalCountForSearchV2(CommentSearchCriteria criteria) {
        return query.selectFrom(comment)
                .where(whereByParams(criteria))
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

    private BooleanExpression[] whereByParams(CommentSearchCriteria criteria) {
        return new BooleanExpression[]{
                userIdEq(criteria.getUserId()),
                itemIdEq(criteria.getItemId())
        };
    }

    private BooleanExpression whereByStatefulParams(CommentSearchCriteria criteria) {
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

    private String getNextSearchAfter(List<Comment> result, CommentSearchCriteria criteria) {
        String nextSearchAfter = null;
        if (criteria.getSize() == result.size()) {
            Comment comment = result.get(result.size() - 1);
            log.info("last comment = {}", comment);
            switch (criteria.getSortType()) {
                case RECENT:
                    nextSearchAfter = SearchAfterEncoder.encode(comment.getId().toString());
                    break;

                //case STAR:
                //    nextSearchAfter = SearchAfterEncoder.encode(comment.getId().toString(), comment.getStar().toString());
                //    break;
                case ITEM:
                    break;
                default: // todo: throw custom exception
                    return null;
            }
        }
        return nextSearchAfter;
    }

    private Long getTotalCount(CommentSearchCriteria criteria) {
        if (!criteria.isWithTotalCount()) {
            return null;
        }
        return query.selectFrom(comment).where(whereBySort(criteria)).fetchCount();
    }


    private BooleanExpression[] whereBySort(CommentSearchCriteria criteria) {
        return new BooleanExpression[]{userIdEq(criteria.getUserId()), itemIdEq(criteria.getItemId())};
    }

    private BooleanExpression nextSearchProduct(CommentSearchCriteria criteria) {
        if (criteria.getNextSearchAfter() == null) {
            return null;
        }

        switch (criteria.getSortType()) {
            case RECENT: {
                String decoded = SearchAfterEncoder.decodeSingle(criteria.getNextSearchAfter());
                Long id = Long.valueOf(decoded);
                return comment.id.lt(id);
            }
            case ITEM: {

            }
            //case STAR: {
            //    String[] decoded = SearchAfterEncoder.decode(criteria.getNextSearchAfter());
            //    Long id = Long.valueOf(decoded[0]);
            //    Integer star = Integer.valueOf(decoded[1]);
            //    return comment.star.lt(star)
            //            .or(
            //                    comment.id.lt(id).and(comment.star.eq(star))
            //            );
            //}
            default:// todo: throw custom exception
                return null;
        }
    }

    private OrderSpecifier<?>[] orderBySort(CommentSearchCriteria criteria) {
        switch (criteria.getSortType()) {
            case RECENT:
                return new OrderSpecifier[]{
                        comment.id.desc()
                };
            //case STAR:
            //    return new OrderSpecifier[]{
            //            comment.star.desc(),
            //            comment.id.desc(),
            //    };
            case ITEM:
            default:
                return null;
        }
    }

    private BooleanExpression itemIdEq(Long itemId) {
        return itemId == null ? null : comment.item.id.eq(itemId);
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId == null ? null : comment.user.id.eq(userId);
    }
}
