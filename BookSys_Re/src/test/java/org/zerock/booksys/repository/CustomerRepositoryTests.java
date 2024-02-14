package org.zerock.booksys.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.booksys.domain.Customer;
import org.zerock.booksys.domain.CustomerRole;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class CustomerRepositoryTests {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void TestInsert() throws Exception{
        //given
        Customer customer = Customer.builder()
                .cId("member0@aaa.bbb")
                .cpw(passwordEncoder.encode("1111"))
                .name("홍길동")
                .phoneNumber("01012345678")
                .build();

        customer.addRole(CustomerRole.USER);
        //when

        //then
        customerRepository.save(customer);
    }

    @Test
    public void TestInsert2() throws Exception{
        //given
        IntStream.range(1,100).forEach(i ->{
            Customer customer = Customer.builder()
                    .cId("member" + i + "@aaa.bbb")
                    .cpw(passwordEncoder.encode("1111"))
                    .name("홍길동")
                    .phoneNumber("01012345678")
                    .build();

            customer.addRole(CustomerRole.USER);

            if(i >= 90){
                customer.addRole(CustomerRole.ADMIN);
            }
            customerRepository.save(customer);
        });
    }

    @Test
    public void testRead(){
        Optional<Customer> result = customerRepository.getWithRoles("member92@aaa.bbb");

        Customer customer = result.orElseThrow();

        log.info(customer);
        log.info(customer.getRoleSet());

        customer.getRoleSet().forEach(customerRole -> log.info(customerRole.name()));
    }

}
