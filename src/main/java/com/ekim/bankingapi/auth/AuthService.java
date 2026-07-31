package com.ekim.bankingapi.auth;

import com.ekim.bankingapi.customer.Customer;
import com.ekim.bankingapi.customer.CustomerService;
import com.ekim.bankingapi.exception.DuplicateResourceException;
import com.ekim.bankingapi.exception.InvalidCredentialsException;
import com.ekim.bankingapi.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        Customer customer = customerService.findCustomerEntityById(request.getCustomerId());

        if (userRepository.existsByCustomerId(request.getCustomerId())) {
            log.warn("Registration rejected - customer already has account: customerId={}", request.getCustomerId());
            throw new DuplicateResourceException("This customer already has a login account");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration rejected - email already in use: email={}", request.getEmail());
            throw new DuplicateResourceException("Email already in use: " + request.getEmail());
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCustomer(customer);

        User saved = userRepository.save(user);

        String token = jwtService.generateToken(saved.getEmail(), saved.getId(), customer.getId());

        log.info("Registration successful: userId={}, customerId={}", saved.getId(), customer.getId());

        return new AuthResponse(saved.getId(), customer.getId(), saved.getEmail(), "Registration successful", token);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed - email not found: email={}", request.getEmail());
                    return new InvalidCredentialsException("Invalid email or password");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Login failed - wrong password: email={}", request.getEmail());
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getId(), user.getCustomer().getId());

        log.info("Login successful: userId={}, email={}", user.getId(), user.getEmail());

        return new AuthResponse(user.getId(), user.getCustomer().getId(), user.getEmail(), "Login successful", token);
    }
}