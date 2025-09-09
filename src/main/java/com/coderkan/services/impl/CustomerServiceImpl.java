package com.coderkan.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import com.coderkan.models.Customer;
import com.coderkan.repositories.CustomerRepository;
import com.coderkan.services.CustomerService;

@Service
@CacheConfig(cacheNames = "customerCache")
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	@CacheEvict(value = "customers", allEntries = true)
	@CachePut(value = "customer", key = "#result.id")
	public Customer add(Customer customer) {
		return this.customerRepository.save(customer);
	}

	@Override
	@Cacheable(value = "customer", key = "#id", unless = "#result == null")	// if result == null -> don't cache
	public Customer getCustomerById(long id) {
		// waitSomeTime();
		return this.customerRepository.findById(id).orElse(null);
	}

	@Override
	@Cacheable(value = "customers")
	public List<Customer> getAll() {
		// waitSomeTime();
		return this.customerRepository.findAll();
	}

	@Override
	@CacheEvict(value = "customers", allEntries = true) //works
	@CachePut(value = "customer", key = "#result.id")
	public Customer update(Customer customer) {
		Optional<Customer> optCustomer = customerRepository.findById(customer.getId());
		if (!optCustomer.isPresent()) {
			return null;
		}
		Customer repCustomer = optCustomer.get();
		repCustomer.setName(customer.getName());
		repCustomer.setAge(customer.getAge());
		repCustomer.setEmail(customer.getEmail());
		return customerRepository.save(repCustomer);
	}

	@Override
	@Caching(evict = {@CacheEvict(value = "customer", key = "#id"),
				      @CacheEvict(value = "customers", allEntries = true)})
	public void delete(long id) {
		this.customerRepository.deleteById(id);
	}

	@Override
	@Cacheable(value = "customersByEmail", key = "#email")
	public Optional<Customer> getCustomerByEmail(String email) {
		System.out.println("Fetching customers from database for email: " + email);
		return customerRepository.findByEmail(email);
	}

	@Override
	public List<Customer> searchCustomersByName(String name) {
		return customerRepository.findByNameContainingIgnoreCase(name);
	}

	@Override
	public List<Customer> getCustomersByAgeRange(Integer minAge, Integer maxAge) {
		return customerRepository.findByAgeBetween(minAge, maxAge);
	}

	@Override
	@CacheEvict(value = {"customer", "customers", "customersByEmail"}, allEntries = true)
	public void clearCache() {
		System.out.println("All caches cleared");
	}

	private void waitSomeTime() {
		System.out.println("Long Wait Begin");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Long Wait End");
	}
}