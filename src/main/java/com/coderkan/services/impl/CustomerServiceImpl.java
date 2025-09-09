package com.coderkan.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
	@CacheEvict(cacheNames = "customers", allEntries = true)
	public Customer add(Customer customer) {
		return this.customerRepository.save(customer);
	}

	@Override
	@Cacheable(cacheNames = "customer", key = "#id", unless = "#result == null")
	public Customer getCustomerById(long id) {
		// waitSomeTime();
		return this.customerRepository.findById(id).orElse(null);
	}

	@Override
	@Cacheable(cacheNames = "customers")
	public List<Customer> getAll() {
		// waitSomeTime();
		return this.customerRepository.findAll();
	}

	@Override
	@CacheEvict(cacheNames = "customers", allEntries = true)
	public Customer update(Customer customer) {
		Optional<Customer> optCustomer = this.customerRepository.findById(customer.getId());
		if (!optCustomer.isPresent()) {
			return null;
		}
		Customer repCustomer = optCustomer.get();
		repCustomer.setName(customer.getName());
		repCustomer.setContactName(customer.getContactName());
		repCustomer.setAddress(customer.getAddress());
		repCustomer.setCity(customer.getCity());
		repCustomer.setPostalCode(customer.getPostalCode());
		repCustomer.setCountry(customer.getCountry());
		return this.customerRepository.save(repCustomer);
	}

	@Override
	@Caching(evict = {@CacheEvict(cacheNames = "customer", key = "#id"),
			@CacheEvict(cacheNames = "customers", allEntries = true)})
	public void delete(long id) {
		this.customerRepository.deleteById(id);
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