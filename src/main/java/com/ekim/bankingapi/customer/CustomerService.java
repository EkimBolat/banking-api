package com.ekim.bankingapi.customer;

import com.ekim.bankingapi.exception.DuplicateResourceException;
import com.ekim.bankingapi.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer createCustomer(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + customer.getEmail());
        }
        if (customerRepository.existsByNationalId(customer.getNationalId())) {
            throw new DuplicateResourceException("National ID already registered: " + customer.getNationalId());
        }
        return customerRepository.save(customer);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer updateCustomer(Long id, Customer updatedData) {
        Customer existing = getCustomerById(id);
        existing.setFirstName(updatedData.getFirstName());
        existing.setLastName(updatedData.getLastName());
        existing.setPhoneNumber(updatedData.getPhoneNumber());
        existing.setAge(updatedData.getAge());
        existing.setAddress(updatedData.getAddress());
        return customerRepository.save(existing);
    }

    public void deleteCustomer(Long id) {
        Customer existing = getCustomerById(id);
        customerRepository.delete(existing);
    }
}