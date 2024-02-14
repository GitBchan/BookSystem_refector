package org.zerock.booksys.dto;

import lombok.Getter;

@Getter
public enum ArrivalTime {
    TIME_08_10(0),
    TIME_10_12(1),
    TIME_12_14(2),
    TIME_14_16(3),
    TIME_16_18(4),
    TIME_18_20(5),
    TIME_20_22(6);

    private final int value;

    private ArrivalTime(int value) {
        this.value = value;
    }
}
