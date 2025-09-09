package com.coderkan.services;

import java.util.List;
import java.util.Optional;

import com.coderkan.models.Customer;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public interface CustomerService {

	public Customer add(Customer customer);

	public Customer getCustomerById(long id);

	public List<Customer> getAll();

	public Customer update(Customer customer);

	public void delete(long id);

	// ******************* Extra Methods *******************/
	public Optional<Customer> getCustomerByEmail(String email);

	public List<Customer> searchCustomersByName(String name);

	public List<Customer> getCustomersByAgeRange(Integer minAge, Integer maxAge);

	public void clearCache();
	// ******************* Extra Methods *******************/
}
