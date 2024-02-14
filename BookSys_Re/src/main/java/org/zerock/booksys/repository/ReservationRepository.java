package org.zerock.booksys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.booksys.domain.Reservation;
import org.zerock.booksys.dto.ArrivalTime;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

   /* @Query(value = "SELECT * FROM reservation m WHERE m.cid = :cid", nativeQuery = true)
    List<Reservation> findReservation(@Param("cid")String cid);

    @Query(value = "SELECT * FROM reservation m WHERE m.rno = :rno", nativeQuery = true)
    List<Reservation> findReservation(@Param("rno")Long rno);

    @Query(value = "SELECT * FROM reservation", nativeQuery = true)
    List<Reservation> findAllReservation();*/

    List<Reservation> findByTableNumberAndArrivalTime(int tableNumber, ArrivalTime arrivalTime);

    @Transactional
    @Modifying
    @Query(value = "Update reservation m set m.arrival_time = :arrivaltime, m.table_number = :tableno where m.reservation_id = :rno", nativeQuery = true)
    void modifyArrivalTimeAndTableNo(@Param("rno") Long rno, @Param("arrivaltime") int arrivaltime, @Param("tableno") int tableno);

    void removeByRno(Long rno);

    @Query(value = "SELECT * FROM reservation m WHERE m.customer_id = :cid AND m.selected_date = (SELECT a.selected_date FROM reservation a WHERE a.reservation_id = :rno)", nativeQuery = true)
    List<Reservation> findReservationSameDate(@Param("cid") String cid, @Param("rno") Long rno);

    @Query(value = "SELECT * FROM reservation m WHERE m.selected_date = (SELECT a.selected_date FROM reservation a WHERE a.reservation_id = :rno)", nativeQuery = true)
    List<Reservation> findReservationSameDate(@Param("rno") Long rno);

    @Transactional
    @Modifying
    @Query(value = "UPDATE reservation m SET m.table_number = 0, m.arrival_time = NULL where m.customer_id = :cid AND m.table_number = :tableno AND m.arrival_time = :arrivaltime", nativeQuery = true)
    void removeReservation(@Param("cid") String cid, @Param("tableno") int tableno, @Param("arrivaltime") int arrivaltime);

    @Query("select r from Reservation r where r.customer.cId = :cId and r.selectedDate >= :selectedDate")
    List<Reservation> findReservationBySelectedDateAfter(@Param("cId")String cid ,@Param("selectedDate")LocalDate selectedDate);

    default List<Reservation> findReservationBySelectedDateAfter(@Param("cId")String cId){
        return findReservationBySelectedDateAfter(cId, LocalDate.now());
    }

    @Query("select r from Reservation r where r.customer.cId = :cId and r.selectedDate < :selectedDate")
    List<Reservation> findReservationBySelectedDateBefore(@Param("cId") String cId, @Param("selectedDate")LocalDate selectedDate);

    default List<Reservation> findReservationBySelectedDateBefore(@Param("cId")String cId) {
        return findReservationBySelectedDateBefore(cId, LocalDate.now());
    }
}