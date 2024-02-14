package org.zerock.booksys.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.booksys.domain.Review;
import org.zerock.booksys.dto.PageRequestDTO;
import org.zerock.booksys.dto.PageResponseDTO;
import org.zerock.booksys.dto.ReviewDTO;
import org.zerock.booksys.dto.ReviewListAllDTO;
import org.zerock.booksys.repository.ReservationRepository;
import org.zerock.booksys.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ModelMapper modelMapper;
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public Long register(ReviewDTO reviewDTO, Long rno) {
        Review review = dtoToEntity(reviewDTO, rno);

        Long reno = reviewRepository.save(review).getReno();

        return reno;
    }

    @Override
    public ReviewDTO readOne(Long reno) {
        Optional<Review> result = reviewRepository.findByIdWithImages(reno);

        Review review = result.orElseThrow();

        ReviewDTO reviewDTO = entityToDTO(review);

        return reviewDTO;
    }

    @Override
    public void modify(ReviewDTO reviewDTO) {
        Optional<Review> result = reviewRepository.findById(reviewDTO.getReno());

        Review review = result.orElseThrow();

        review.change(reviewDTO.getTitle(),review.getContent());


        if(reviewDTO.getFileNames() != null){
            for(String fileName : reviewDTO.getFileNames()){
                String [] arr = fileName.split("_");
                review.addImage(arr[0], arr[1]);
            }
        }

        reviewRepository.save(review);
    }

    @Override
    public void remove(Long reno) {
        reviewRepository.deleteById(reno);
    }

    @Override
    public PageResponseDTO<ReviewDTO> list(PageRequestDTO pageRequestDTO) {
        String [] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("reno");

        Page<Review> result = reviewRepository.searchAll(types, keyword, pageable);

        List<ReviewDTO> dtoList = result.getContent().stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class)).collect(Collectors.toList());

        return PageResponseDTO.<ReviewDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<ReviewListAllDTO> listWithAll(PageRequestDTO pageRequestDTO) {
        String [] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("reno");

        Page<ReviewListAllDTO> result = reviewRepository.searchWithAll(types, keyword, pageable);

        return PageResponseDTO.<ReviewListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int) result.getTotalElements())
                .build();
    }

    Review dtoToEntity(ReviewDTO reviewDTO, Long rno){
        Review review = Review.builder()
                .reno(reviewDTO.getReno())
                .title(reviewDTO.getTitle())
                .content(reviewDTO.getContent())
                .writer(reviewDTO.getWriter())
                .reservation(reservationRepository.findById(rno).orElseThrow())
                .build();

        if(reviewDTO.getFileNames() != null){
            reviewDTO.getFileNames().forEach(fileName ->{
                String [] arr = fileName.split("_");
                review.addImage(arr[0], arr[1]);
            });
        }
        return review;
    }

    ReviewDTO entityToDTO(Review review){
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .reno(review.getReno())
                .title(review.getTitle())
                .content(review.getContent())
                .writer(review.getWriter())
                .rno(review.getReservation().getRno())
                .build();

        List<String> fileNames = review.getReviewImages().stream().sorted().map(reviewImage -> reviewImage.getUuid() + "_"
                + reviewImage.getFilename()).collect(Collectors.toList());

        reviewDTO.setFileNames(fileNames);

        return reviewDTO;
    }
}
