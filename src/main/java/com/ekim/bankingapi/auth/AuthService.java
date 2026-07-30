package com.ekim.bankingapi.auth;

import com.ekim.bankingapi.customer.Customer;
import com.ekim.bankingapi.customer.CustomerRepository;
import com.ekim.bankingapi.exception.DuplicateResourceException;
import com.ekim.bankingapi.exception.InvalidCredentialsException;
import com.ekim.bankingapi.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(Long customerId, String email, String rawPassword) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        if (userRepository.existsByCustomerId(customerId)) {
            throw new DuplicateResourceException("This customer already has a login account");
        }
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email already in use: " + email);
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setCustomer(customer);

        return userRepository.save(user);
    }

    public User login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return user;
    }
}