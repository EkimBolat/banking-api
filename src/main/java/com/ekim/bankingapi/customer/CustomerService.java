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

    public CustomerResponse createCustomer(CustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + request.getEmail());
        }
        if (customerRepository.existsByNationalId(request.getNationalId())) {
            throw new DuplicateResourceException("National ID already registered: " + request.getNationalId());
        }

        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setNationalId(request.getNationalId());
        customer.setAge(request.getAge());
        customer.setAddress(request.getAddress());

        Customer saved = customerRepository.save(customer);
        return CustomerResponse.fromEntity(saved);
    }

    public CustomerResponse getCustomerById(Long id) {
        Customer customer = findCustomerEntityById(id);
        return CustomerResponse.fromEntity(customer);
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(CustomerResponse::fromEntity)
                .toList();
    }

    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer existing = findCustomerEntityById(id);
        existing.setFirstName(request.getFirstName());
        existing.setLastName(request.getLastName());
        existing.setPhoneNumber(request.getPhoneNumber());
        existing.setAge(request.getAge());
        existing.setAddress(request.getAddress());

        Customer saved = customerRepository.save(existing);
        return CustomerResponse.fromEntity(saved);
    }

    public void deleteCustomer(Long id) {
        Customer existing = findCustomerEntityById(id);
        customerRepository.delete(existing);
    }

    // Diğer feature'lar (Account, Auth) Customer entity'sine ihtiyaç duyduğu için bunu koruyoruz
    public Customer findCustomerEntityById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }
}