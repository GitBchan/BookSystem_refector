package org.zerock.booksys.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.booksys.domain.Customer;
import org.zerock.booksys.dto.CustomerSecurityDTO;
import org.zerock.booksys.repository.CustomerRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUserName: " + username);

        Optional<Customer> result =  customerRepository.getWithRoles(username);

        if(result.isEmpty()){
            throw new UsernameNotFoundException("username not found...");
        }

        Customer customer = result.orElseThrow();

        CustomerSecurityDTO customerSecurityDTO = new CustomerSecurityDTO(
                customer.getcId(),
                customer.getCpw(),
                customer.getName(),
                customer.getPhoneNumber(),
                customer.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name()))
                        .collect(Collectors.toList())
        );

        log.info("customerSecurityDTO: " + customerSecurityDTO);

        return customerSecurityDTO;
    }
}
