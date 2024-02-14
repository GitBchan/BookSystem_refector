package org.zerock.booksys.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "review")
@Table(name = "review_image")
public class ReviewImage implements Comparable<ReviewImage>{

    @Id
    private String uuid;
    private String filename;
    private int ord;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Override
    public int compareTo(ReviewImage other) {
        return this.ord - other.ord;
    }

    public void changeReview(Review review){
        this.review = review;
    }
}
