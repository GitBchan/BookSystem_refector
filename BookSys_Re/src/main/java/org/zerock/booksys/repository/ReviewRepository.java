package org.zerock.booksys.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.booksys.domain.Review;
import org.zerock.booksys.repository.search.ReviewSearch;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewSearch {
    @EntityGraph(attributePaths = {"reviewImages"})
    @Query("select r from Review r where r.reno = :reno")
    Optional<Review> findByIdWithImages(Long reno);
}
