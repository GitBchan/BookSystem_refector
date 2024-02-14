package org.zerock.booksys.dto;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@ToString
public class CustomerSecurityDTO extends User {
    private String cId;
    private String cpw;
    private String name;
    private String phoneNumber;

    public CustomerSecurityDTO(String username, String password, String name, String phoneNumber,Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.cId = username;
        this.cpw = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
