package org.zerock.booksys.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.booksys.domain.Reservation;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Log4j2
public class ReservationRepositoryTests {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void testFindReservationBySelectedDateAfter() throws Exception{
        //given
        List<Reservation> result = reservationRepository.findReservationBySelectedDateAfter("member20@aaa.bbb", LocalDate.now());
        for (Reservation reservation : result) {
            log.info(reservation);
        }
    }
}
