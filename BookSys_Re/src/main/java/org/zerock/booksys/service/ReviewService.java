package org.zerock.booksys.service;

import org.zerock.booksys.domain.Review;
import org.zerock.booksys.dto.PageRequestDTO;
import org.zerock.booksys.dto.PageResponseDTO;
import org.zerock.booksys.dto.ReviewDTO;
import org.zerock.booksys.dto.ReviewListAllDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface ReviewService {
    Long register(ReviewDTO reviewDTO, Long rno);
    ReviewDTO readOne(Long reno);
    void modify(ReviewDTO reviewDTO);
    void remove(Long reno);
    PageResponseDTO<ReviewDTO> list(PageRequestDTO pageRequestDTO);
    PageResponseDTO<ReviewListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

}
