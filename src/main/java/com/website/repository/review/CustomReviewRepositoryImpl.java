package com.website.repository.review;


import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.website.repository.common.PageResult;
import com.website.repository.model.item.Item;
import com.website.repository.model.user.User;
import com.website.repository.review.model.QReview;
import com.website.repository.review.model.Review;
import com.website.repository.review.model.ReviewSearchCriteria;
import com.website.utils.common.SearchAfterEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Slf4j
public class CustomReviewRepositoryImpl extends QuerydslRepositorySupport implements CustomReviewRepository {

    private final QReview review = QReview.review;

    public CustomReviewRepositoryImpl() {
        super(Review.class);
    }


    @Override
    public PageResult<Review> search(ReviewSearchCriteria criteria) {
        List<Review> result = from(review)
                .where(userEq(criteria.getUser()), itemEq(criteria.getItem()), nextSearchProduct(criteria))
                .limit(criteria.getSize())
                .orderBy(orderBySort(criteria))
                .fetch();

        Long totalCount = getTotalCount(criteria);

        String nextSearchAfter = getNextSearchAfter(result, criteria);

        return PageResult.<Review>builder()
                .items(result)
                .getNextSearchAfter(nextSearchAfter)
                .totalCount(totalCount)
                .build();
    }

    private String getNextSearchAfter(List<Review> result, ReviewSearchCriteria criteria) {
        String nextSearchAfter = null;
        if (criteria.getSize() == result.size()) {
            Review review = result.get(result.size() - 1);
            log.info("last review = {}", review);
            switch (criteria.getSortType()) {
                case RECENT:
                    nextSearchAfter = SearchAfterEncoder.encode(review.getId().toString());
                    break;

                case STAR:
                    nextSearchAfter = SearchAfterEncoder.encode(review.getId().toString(), review.getStar().toString());
                    break;
                case ITEM:
                    break;
                default: // todo: throw custom exception
                    return null;
            }
        }
        return nextSearchAfter;
    }

    private Long getTotalCount(ReviewSearchCriteria criteria) {
        if (!criteria.isWithTotalCount()) {
            return null;
        }
        return from(review).where(whereBySort(criteria)).fetchCount();
    }


    private BooleanExpression[] whereBySort(ReviewSearchCriteria criteria) {
        return new BooleanExpression[]{userEq(criteria.getUser()), itemEq(criteria.getItem())};
    }

    private BooleanExpression nextSearchProduct(ReviewSearchCriteria criteria) {
        if (criteria.getNextSearchAfter() == null) {
            return null;
        }

        switch (criteria.getSortType()) {
            case RECENT: {
                String decoded = SearchAfterEncoder.decodeSingle(criteria.getNextSearchAfter());
                Long id = Long.valueOf(decoded);
                log.info("id = {}", id);
                return review.id.lt(id);
            }
            case ITEM: {

            }
            case STAR: {
                String[] decoded = SearchAfterEncoder.decode(criteria.getNextSearchAfter());
                Long id = Long.valueOf(decoded[0]);
                Integer star = Integer.valueOf(decoded[1]);
                return review.star.lt(star)
                        .or(
                                review.id.lt(id).and(review.star.eq(star))
                        );
            }
            default:// todo: throw custom exception
                return null;
        }
    }

    private OrderSpecifier<?>[] orderBySort(ReviewSearchCriteria criteria) {
        switch (criteria.getSortType()) {
            case RECENT:
                return new OrderSpecifier[]{
                        review.id.desc()
                };
            case STAR:
                return new OrderSpecifier[]{
                        review.star.desc(),
                        review.id.desc(),
                };
            case ITEM:
            default:
                return null;
        }
    }

    private BooleanExpression itemEq(Item item) {
        return item == null ? null : review.item.eq(item);
    }

    private BooleanExpression userEq(User user) {
        return user == null ? null : review.user.eq(user);
    }
}
