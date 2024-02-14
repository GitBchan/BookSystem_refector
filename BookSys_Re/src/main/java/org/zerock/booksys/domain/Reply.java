package org.zerock.booksys.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Reply", indexes = {
        @Index(name = "idx_reply_review_reno", columnList = "review_review_id")
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "review")
public class Reply extends BaseEntity{
    @Id
    @Column(name = "reply_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyNo;
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;
    private String replyText;
    private String replyer;
    public void changeText(String replyText){
        this.replyText = replyText;
    }
}
