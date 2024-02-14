package org.zerock.booksys.domain;

import lombok.*;
import org.zerock.booksys.dto.ArrivalTime;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservation")
@ToString(exclude = "customer")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long rno;
    private int number;
    private LocalDate selectedDate;
    private int tableNumber;

    @Enumerated(EnumType.ORDINAL)
    private ArrivalTime arrivalTime;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public void changeReservation(LocalDate selectedDate, int number) {
        this.selectedDate = selectedDate;
        this.number = number;
    }
}
