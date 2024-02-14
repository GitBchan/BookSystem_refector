package org.zerock.booksys.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewListAllDTO {
    private Long reno;
    private String title;
    private String writer;
    private LocalDateTime regDate;
    private Long replyCount;
    private List<ReviewImageDTO> reviewImages;
}
