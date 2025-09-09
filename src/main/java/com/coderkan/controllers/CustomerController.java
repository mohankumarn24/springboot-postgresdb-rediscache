package com.coderkan.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.coderkan.models.Customer;
import com.coderkan.services.CustomerService;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@PostMapping
	public ResponseEntity<Object> addCustomer(@RequestBody Customer customer) {
		Customer customerAdded = customerService.add(customer);
		return ResponseEntity.status(HttpStatus.CREATED).body(customerAdded);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Object> getCustomerById(@PathVariable("id") String id) {
		Long customerId = Long.valueOf(id);
		Customer customer = customerService.getCustomerById(customerId);
		return ResponseEntity.ok(customer);
	}

	@GetMapping
	public ResponseEntity<Object> getAllCustomers() {
		List<Customer> customers = customerService.getAll();
		return ResponseEntity.ok(customers);
	}

	@PutMapping
	public ResponseEntity<Object> updateCustomer(@RequestBody Customer customer) {
		Customer customerUpdated = customerService.update(customer);
		return ResponseEntity.ok(customerUpdated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteCustomerById(@PathVariable("id") String id) {
		Long customerId = Long.valueOf(id);
		customerService.delete(customerId);
		return ResponseEntity.ok().build();
	}

	// ******************* Extra Methods *******************/
	@GetMapping("/email/{email}")
	public ResponseEntity<Object> getCustomerByEmail(@PathVariable String email) {
		Optional<Customer> customer = customerService.getCustomerByEmail(email);
		if (customer.isPresent()) {
			return new ResponseEntity<>(customer.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Customer not found with email: " + email, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/search")
	public ResponseEntity<List<Customer>> searchCustomersByName(@RequestParam String name) {
		List<Customer> users = customerService.searchCustomersByName(name);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@GetMapping("/age-range")
	public ResponseEntity<List<Customer>> getCustomersByAgeRange(@RequestParam Integer minAge, @RequestParam Integer maxAge) {
		List<Customer> customers = customerService.getCustomersByAgeRange(minAge, maxAge);
		return new ResponseEntity<>(customers, HttpStatus.OK);
	}

	@PostMapping("/clear-cache")
	public ResponseEntity<String> clearCache() {
		customerService.clearCache();
		return new ResponseEntity<>("Cache cleared successfully", HttpStatus.OK);
	}
	// ******************* Extra Methods *******************/
}
