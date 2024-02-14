package org.zerock.booksys.service;

import org.zerock.booksys.dto.ArrivalTime;
import org.zerock.booksys.dto.ReservationDTO;
import org.zerock.booksys.dto.SelectDayDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    Long register(ReservationDTO reservationDTO);
    ReservationDTO readOne(Long rno);
    boolean checkScheduleOccupied(int tableNumber, ArrivalTime time);
    List<String> getAvailableSchedule(Long rno);
    List<String> getModifiableSchedule(String cid,Long rno);
    String getReservationListTOJSON();
    void resetSchedule(String cid, int tableNumber, ArrivalTime time);
    void modifySchedule(Long rno,int time,int table);
    void removeSchedule(Long rno);
    List<ReservationDTO> getReservationList(String cid);
    void modifyReservation(Long rno, SelectDayDTO selectDayDTO);

    List<ReservationDTO> getReviewReservationList(String cId);
}
