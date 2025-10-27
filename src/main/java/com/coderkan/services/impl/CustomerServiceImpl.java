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
	@CachePut(value = "customer", key = "#result.id", condition = "#result != null")
	public Customer add(Customer customer) {
		return this.customerRepository.save(customer);
	}

	// sync=true ensures only one thread at a time loads a missing cache entry for a given key.
	// Other threads wait for the first thread to finish and then get the cached value. Prevents cache stampede/avalanche
	@Override
	@Cacheable(value = "customer", key = "#id", unless = "#result == null", sync = true)	// if result == null -> don't cache
	public Customer getCustomerById(long id) {
		// waitSomeTime();
		return this.customerRepository.findById(id).orElse(null);
	}

	@Override
	@Cacheable(value = "customers")
	public List<Customer> getAll() {
		return this.customerRepository.findAll();
	}

	@Override
	@CacheEvict(value = "customers", allEntries = true)
	@CachePut(value = "customer", key = "#result.id")
	public Customer update(Customer customer) {
        return customerRepository.findById(customer.getId())
                .map(existing -> {
                    existing.setName(customer.getName());
                    existing.setAge(customer.getAge());
                    existing.setEmail(customer.getEmail());
                    return customerRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Customer with id " + customer.getId() + " not found"));
                // .orElse(null);
	}

	@Override
	@Caching(evict = {
						@CacheEvict(value = "customer", key = "#id"),
						@CacheEvict(value = "customers", allEntries = true)
					}
			)
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
}

/*
Behavior of sync=true: 
 - Thread A requests key X. Cache miss. Thread A loads DB.
 - Thread B requests key X at the same time. Cache miss. Thread B waits for Thread A.
 - Once Thread A finishes and stores the value in cache, Thread B retrieves it from the cache.
 - sync=true, only works for the same cache key. Different keys are not synchronized.
*/