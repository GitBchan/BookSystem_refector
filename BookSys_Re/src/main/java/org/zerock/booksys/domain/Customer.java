package org.zerock.booksys.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer")
public class Customer {

    @Id
    @Column(name = "customer_id")
    private String cId;
    private String cpw;
    private String name;
    private String phoneNumber;
    private boolean social;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<CustomerRole> roleSet = new HashSet<>();

    public void changePassword(String mpw){
        this.cpw = mpw;
    }

    public void addRole(CustomerRole customerRole){ this.roleSet.add(customerRole);}
    public String getcId() {
        return cId;
    }
    public String getCpw() {
        return cpw;
    }
    public String getName() {
        return name;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public boolean isSocial() {
        return social;
    }
    public Set<CustomerRole> getRoleSet() {
        return roleSet;
    }
}
