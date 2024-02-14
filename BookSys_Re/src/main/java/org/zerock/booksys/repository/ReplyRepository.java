package org.zerock.booksys.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.booksys.domain.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("select r from Reply r where r.review.reno = :reno")
    Page<Reply> listOfBoard(Long reno, Pageable pageable);

    void deleteByReview_Reno(Long reno);
}
