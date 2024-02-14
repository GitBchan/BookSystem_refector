package org.zerock.booksys.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.booksys.dto.ReviewDTO;

@SpringBootTest
@Log4j2
public class ReviewServiceTests {

    @Autowired
    private ReviewService reviewService;

    @Test
    public void registerTest() throws Exception{
        //given
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .title("test title")
                .content("test content")
                .writer("user20")
                .rno(1L)
                .build();

        Long rno = 1L;
        //when

        //then
        Long result = reviewService.register(reviewDTO, rno);

        log.info(result);
    }

    @Test
    public void readOneTest() throws Exception{
        //given
        Long reno = 3L;
        //when

        //then
        ReviewDTO result = reviewService.readOne(reno);

        log.info(result);
    }
    
    @Test
    public void modifyTest() throws Exception{
        //given
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .reno(3L)
                .title("modify title")
                .content("modify content")
                .writer("user20")
                .rno(1L)
                .build();
        //when
        
        //then
        reviewService.modify(reviewDTO);

        ReviewDTO result = reviewService.readOne(reviewDTO.getReno());

        log.info(result);
    }
}
