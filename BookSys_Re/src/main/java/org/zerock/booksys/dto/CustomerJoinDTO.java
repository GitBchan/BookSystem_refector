package org.zerock.booksys.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerJoinDTO {
    private String cId;
    private String cpw;
    private String name;
    private String phoneNumber;
    private boolean social;
}
