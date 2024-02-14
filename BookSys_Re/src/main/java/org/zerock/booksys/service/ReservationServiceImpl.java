package org.zerock.booksys.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.booksys.domain.Customer;
import org.zerock.booksys.domain.Reservation;
import org.zerock.booksys.dto.ArrivalTime;
import org.zerock.booksys.dto.ReservationDTO;
import org.zerock.booksys.dto.SelectDayDTO;
import org.zerock.booksys.repository.ReservationRepository;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService{

    private final ReservationRepository reservationRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long register(ReservationDTO reservationDTO) {
        Reservation reservation = modelMapper.map(reservationDTO, Reservation.class);
        Long rno = reservationRepository.save(reservation).getRno();
        log.info("Insert rno: " + rno);
        return rno;
    }

    @Override
    public ReservationDTO readOne(Long rno) {
        Reservation reservation = reservationRepository.findById(rno).orElseThrow();

        return modelMapper.map(reservation, ReservationDTO.class);
    }

    /**
     * 해당 스케쥴이 예약 되어있는지 확인
     * @param tableNumber
     * @param time
     * @return
     */
    @Override
    public boolean checkScheduleOccupied(int tableNumber, ArrivalTime time) {
        List<Reservation> list = reservationRepository.findByTableNumberAndArrivalTime(tableNumber, time);
        if(list.isEmpty())
            return false;
        for (Reservation reservation : list) {
            if(reservation.getArrivalTime() == time && reservation.getTableNumber() == tableNumber)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> getAvailableSchedule(Long rno) {
        List<Reservation> list = reservationRepository.findReservationSameDate(rno);
        return list.stream().map(r -> {
            if(r.getTableNumber() == 0)
                return null;
            else
                return r.getTableNumber() +"_"+r.getArrivalTime().getValue();
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> getModifiableSchedule(String cid, Long rno) {
        List<Reservation> list = reservationRepository.findReservationSameDate(cid,rno);
        return list.stream().map(r -> {
            if(r.getTableNumber() == 0)
                return null;
            else
                return r.getTableNumber() +"_"+r.getArrivalTime().getValue();
        }).collect(Collectors.toList());
    }

    @Override
    public String getReservationListTOJSON() {
        Gson gson = new Gson();
        List<Reservation> list = reservationRepository.findAll();

        JsonObject main = new JsonObject();

        for(Reservation reservation : list){
            if(reservation.getSelectedDate() == null)
                continue;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String date = simpleDateFormat.format(reservation.getSelectedDate());
            Customer customer = reservation.getCustomer();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", customer.getName());
            jsonObject.addProperty("phone", customer.getPhoneNumber());
            jsonObject.addProperty("date", date);

            main.addProperty(reservation.getRno() + "", gson.toJson(jsonObject));
        }

        String jsonStr = gson.toJson(main);
        return jsonStr;
    }

    @Override
    public void resetSchedule(String cid, int tableNumber, ArrivalTime time) {
        reservationRepository.removeReservation(cid, tableNumber, time.getValue());
    }

    /**
     * Reservation의 테이블 번호나, 도착 시간 변경하는 메서드
     * @param rno
     * @param time
     * @param table
     */
    @Override
    public void modifySchedule(Long rno, int time, int table) {
        reservationRepository.modifyArrivalTimeAndTableNo(rno, time, table);
    }

    /**
     * Reservation 삭제 메서드
     * @param rno
     */
    @Override
    public void removeSchedule(Long rno) {
        reservationRepository.removeByRno(rno);
    }

    @Override
    public List<ReservationDTO> getReservationList(String cid) {
        List<Reservation> result = reservationRepository.findReservationBySelectedDateAfter(cid);

        return result.stream()
                .map(reservation -> modelMapper.map(reservation, ReservationDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void modifyReservation(Long rno, SelectDayDTO selectDayDTO) {
        Reservation reservation = reservationRepository.findById(rno).orElseThrow();

        reservation.changeReservation(selectDayDTO.getSelectedDate(), selectDayDTO.getNumber());
    }

    @Override
    public List<ReservationDTO> getReviewReservationList(String cId) {
        List<Reservation> result = reservationRepository.findReservationBySelectedDateBefore(cId);

        return result.stream()
                .map(reservation -> modelMapper.map(reservation, ReservationDTO.class))
                .collect(Collectors.toList());
    }


}
