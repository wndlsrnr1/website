package com.website.repository.comment;


import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.website.repository.comment.model.CommentWithAnswer;
import com.website.repository.comment.model.QComment;
import com.website.repository.common.PageResult;
import com.website.repository.comment.model.Comment;
import com.website.repository.comment.model.CommentSearchCriteria;
import com.website.utils.common.SearchAfterEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Slf4j
public class CustomCommentRepositoryImpl extends QuerydslRepositorySupport implements CustomCommentRepository {

    private final QComment comment = QComment.comment;

    public CustomCommentRepositoryImpl() {
        super(Comment.class);
    }


    @Override
    public PageResult<Comment> search(CommentSearchCriteria criteria) {
        List<Comment> result = from(comment)
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
        return null;
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
        return from(comment).where(whereBySort(criteria)).fetchCount();
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
