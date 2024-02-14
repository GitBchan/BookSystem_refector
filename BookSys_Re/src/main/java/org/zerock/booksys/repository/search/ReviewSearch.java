package org.zerock.booksys.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.booksys.domain.Review;
import org.zerock.booksys.dto.ReviewListAllDTO;

public interface ReviewSearch {
    Page<Review> search(Pageable pageable);
    Page<Review> searchAll(String [] types, String keyword, Pageable pageable);
    Page<ReviewListAllDTO> searchWithAll(String [] types, String keyword, Pageable pageable);
}
