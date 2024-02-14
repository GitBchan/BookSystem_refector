package org.zerock.booksys.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "review")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long reno;

    @OneToMany(mappedBy = "review",
            cascade = {CascadeType.ALL},
            fetch = LAZY,
            orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 20)
    private Set<ReviewImage> reviewImages = new HashSet<>();

    @Column(length = 500, nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public void addImage(String uuid, String filename){
        ReviewImage reviewImage = ReviewImage.builder()
                .uuid(uuid)
                .filename(filename)
                .review(this)
                .ord(reviewImages.size())
                .build();

        reviewImages.add(reviewImage);
    }

    public void clearImage(){
        reviewImages.forEach(reviewImage -> reviewImage.changeReview(null));

        this.reviewImages.clear();
    }

    public void change(String title, String content){
        this.title = title;
        this.content = content;
    }


}
