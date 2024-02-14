package org.zerock.booksys.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.booksys.domain.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    @EntityGraph(attributePaths = "roleSet")
    @Query("select c from Customer c where c.cId = :cid and c.social = false")
    Optional<Customer> getWithRoles(String cid);

    @Modifying
    @Transactional
    @Query("UPDATE Customer c set c.cpw = :cpw where c.cId = :cid")
    void updatePassword(@Param("cpw")String password, @Param("cid")String cid);


}
