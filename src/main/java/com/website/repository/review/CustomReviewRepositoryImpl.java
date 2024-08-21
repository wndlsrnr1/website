package com.website.repository.review;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.website.exception.ClientException;
import com.website.exception.ErrorCode;
import com.website.repository.common.PageResult;
import com.website.repository.review.model.QReview;
import com.website.repository.review.model.Review;
import com.website.repository.review.model.ReviewSortType;
import com.website.service.review.model.ReviewSearchCriteria;
import com.website.utils.common.SearchAfterEncoder;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class CustomReviewRepositoryImpl extends QuerydslRepositorySupport implements CustomReviewRepository {

    private final QReview review = QReview.review;

    public CustomReviewRepositoryImpl() {
        super(Review.class);
    }

    @Override
    public PageResult<Review> search(ReviewSearchCriteria criteria) {
        List<Review> result = from(review)
                .where(
                        itemIdEq(criteria.getItemId()),
                        userIdEq(criteria.getUserId()),
                        nextSearchReviews(criteria)
                )
                .orderBy(orderBySort(criteria.getSortType()))
                .limit(criteria.getSize())
                .fetch();

        String nextSearchAfter = getNextSearchAfter(criteria, result);

        Long totalCount = getTotalCount(criteria);

        return PageResult.<Review>builder()
                .items(result)
                .nextSearchAfter(nextSearchAfter)
                .totalCount(totalCount)
                .build();
    }

    private Long getTotalCount(ReviewSearchCriteria criteria) {
        if (!criteria.isWithTotalCount()) {
            return null;
        }
        return from(review).where(
                userIdEq(criteria.getUserId()), itemIdEq(criteria.getItemId())
        ).fetchCount();
    }

    private String getNextSearchAfter(ReviewSearchCriteria criteria, List<Review> result) {
        String nextSearchAfter = null;

        if (criteria.getSize() == result.size()) {
            Review lastReview = result.get(result.size() - 1);
            switch (criteria.getSortType()) {
                case RECENT: {
                    nextSearchAfter = SearchAfterEncoder.encode(lastReview.getId().toString());
                    break;
                }
                case STAR_DESC: {
                    nextSearchAfter = SearchAfterEncoder.encode(lastReview.getId().toString(), lastReview.getStar().toString());
                    break;
                }
                case STAR_ASC: {
                    nextSearchAfter = SearchAfterEncoder.encode(lastReview.getId().toString(), lastReview.getStar().toString());
                    break;
                }
                default: {
                    throw new ClientException(ErrorCode.BAD_REQUEST, "unimplemented sort type. sorType = " + criteria.getSortType().name());
                }
            }
        }
        return nextSearchAfter;
    }

    private OrderSpecifier<?>[] orderBySort(ReviewSortType sortType) {
        switch (sortType) {
            case RECENT: {
                return new OrderSpecifier[]{review.id.desc()};
            }
            case STAR_DESC: {
                return new OrderSpecifier[]{review.star.desc(), review.id.desc()};
            }
            case STAR_ASC: {
                return new OrderSpecifier[]{review.star.asc(), review.id.desc()};
            }
            default: {
                throw new ClientException(ErrorCode.BAD_REQUEST, "unimplemented sort type. sorType = " + sortType.name());
            }
        }
    }

    private BooleanExpression nextSearchReviews(ReviewSearchCriteria criteria) {
        if (criteria.getNextSearchAfter() == null) {
            return null;
        }

        switch (criteria.getSortType()) {
            case RECENT: {
                String decoded = SearchAfterEncoder.decodeSingle(criteria.getNextSearchAfter());
                long reviewId = Long.parseLong(decoded);
                return review.id.lt(reviewId);
            }
            case STAR_ASC: {
                String[] decoded = SearchAfterEncoder.decode(criteria.getNextSearchAfter());
                long reviewId = Long.parseLong(decoded[0]);
                int star = Integer.parseInt(decoded[1]);
                return review.star.gt(star)
                        .or(
                                review.id.lt(reviewId).and(review.star.eq(star))
                        );
            }
            case STAR_DESC: {
                String[] decoded = SearchAfterEncoder.decode(criteria.getNextSearchAfter());
                long reviewId = Long.parseLong(decoded[0]);
                int star = Integer.parseInt(decoded[1]);
                return review.star.lt(star)
                        .or(
                                review.id.lt(reviewId).and(review.star.eq(star))
                        );
            }
            default: {
                throw new ClientException(ErrorCode.BAD_REQUEST, "unimplemented sort type. sorType = " + criteria.getSortType().name());
            }
        }
    }


    private BooleanExpression userIdEq(Long userId) {
        return userId == null ? null : review.purchases.user.id.eq(userId);
    }

    private BooleanExpression itemIdEq(Long itemId) {
        return itemId == null ? null : review.purchases.item.id.eq(itemId);
    }
}
