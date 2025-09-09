package com.coderkan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.coderkan.models.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByNameContainingIgnoreCase(String name);

    List<Customer> findByAgeBetween(Integer minAge, Integer maxAge);

    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE c.age > :age")
    List<Customer> findCustomersOlderThan(@Param("age") Integer age);
}

