package org.zerock.booksys.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.booksys.domain.*;
import org.zerock.booksys.dto.ReviewImageDTO;
import org.zerock.booksys.dto.ReviewListAllDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewSearchImpl extends QuerydslRepositorySupport implements ReviewSearch {

    public ReviewSearchImpl() {
        super(Review.class);
    }

    @Override
    public Page<Review> search(Pageable pageable) {
        QReview review = QReview.review;

        JPQLQuery<Review> query = from(review);

        query.where(review.title.contains("1"));

        this.getQuerydsl().applyPagination(pageable, query);

        List<Review> list = query.fetch();

        long count = query.fetchCount();

        return null;
    }

    @Override
    public Page<Review> searchAll(String[] types, String keyword, Pageable pageable) {
        QReview review = QReview.review;
        JPQLQuery<Review> query = from(review);

        BooleanBuilder booleanBuilder = null;
        if((types != null && types.length > 0) && keyword != null){
            booleanBuilder = new BooleanBuilder();

            for (String type : types){
                switch (type){
                    case "t":
                        booleanBuilder.or(review.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(review.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(review.writer.contains(keyword));
                        break;
                }
            }
            query.where(booleanBuilder);
        }
        query.where(review.reno.gt(0L));

        this.getQuerydsl().applyPagination(pageable, query);

        List<Review> list = query.fetch();

        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<ReviewListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {
        QReview review = QReview.review;
        QReply reply = QReply.reply;
        QReviewImage reviewImage = QReviewImage.reviewImage;

        JPQLQuery<Review> reviewJPQLQuery = from(review);
        reviewJPQLQuery.leftJoin(reply).on(reply.review.eq(review))
                .leftJoin(review.reviewImages, reviewImage).fetchJoin();

        if((types != null && types.length > 0) && keyword != null){
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String type: types){
                switch (type){
                    case "t":
                        booleanBuilder.or(review.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(review.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(review.writer.contains(keyword));
                        break;
                }
            }
            reviewJPQLQuery.where(booleanBuilder);
        }

        reviewJPQLQuery.groupBy(review);

        this.getQuerydsl().applyPagination(pageable, reviewJPQLQuery);

        JPQLQuery<Tuple> tupleJPQLQuery = reviewJPQLQuery.select(review, reply.countDistinct());

        List<Tuple> tupleList = tupleJPQLQuery.fetch();

        List<ReviewListAllDTO> dtoList = tupleList.stream().map(tuple -> {
            Review review1 = (Review) tuple.get(review);
            long replyCount = tuple.get(1, Long.class);

            ReviewListAllDTO dto = ReviewListAllDTO.builder()
                    .reno(review1.getReno())
                    .title(review1.getTitle())
                    .writer(review1.getWriter())
                    .regDate(review1.getRegDate())
                    .replyCount(replyCount)
                    .build();

            List<ReviewImageDTO> imageDTOS = review1.getReviewImages().stream().sorted().map(reviewImage1 ->
                    ReviewImageDTO.builder()
                            .uuid(reviewImage1.getUuid())
                            .fileName(reviewImage1.getFilename())
                            .ord(reviewImage1.getOrd())
                            .build()).collect(Collectors.toList());

            dto.setReviewImages(imageDTOS);

            return dto;
        }).collect(Collectors.toList());

        long totalCount = reviewJPQLQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, totalCount);
    }
}
