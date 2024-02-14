package org.zerock.booksys.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectDayDTO {
    private String name;
    private int number;
    private String phoneNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate selectedDate;
    private String customerID;
    public ReservationDTO toReservationDTO(){
        ReservationDTO reservationDTO = ReservationDTO.builder()
                .number(number)
                .selectedDate(selectedDate)
                .customerID(customerID)
                .build();

        return reservationDTO;
    }
}
