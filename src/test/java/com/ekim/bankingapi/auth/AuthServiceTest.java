package com.ekim.bankingapi.auth;

import com.ekim.bankingapi.customer.Customer;
import com.ekim.bankingapi.customer.CustomerService;
import com.ekim.bankingapi.exception.DuplicateResourceException;
import com.ekim.bankingapi.exception.InvalidCredentialsException;
import com.ekim.bankingapi.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
    }

    @Test
    void register_shouldSucceed_whenDataIsValid() {
        RegisterRequest request = new RegisterRequest();
        request.setCustomerId(1L);
        request.setEmail("ahmet@example.com");
        request.setPassword("plain-password");

        when(customerService.findCustomerEntityById(1L)).thenReturn(customer);
        when(userRepository.existsByCustomerId(1L)).thenReturn(false);
        when(userRepository.existsByEmail("ahmet@example.com")).thenReturn(false);
        when(passwordEncoder.encode("plain-password")).thenReturn("hashed-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(jwtService.generateToken(anyString(), anyLong(), anyLong())).thenReturn("fake-jwt-token");

        AuthResponse response = authService.register(request);

        assertThat(response.getToken()).isEqualTo("fake-jwt-token");
        assertThat(response.getEmail()).isEqualTo("ahmet@example.com");
        verify(passwordEncoder).encode("plain-password");
    }

    @Test
    void register_shouldThrow_whenCustomerAlreadyHasAccount() {
        RegisterRequest request = new RegisterRequest();
        request.setCustomerId(1L);
        request.setEmail("ahmet@example.com");
        request.setPassword("plain-password");

        when(customerService.findCustomerEntityById(1L)).thenReturn(customer);
        when(userRepository.existsByCustomerId(1L)).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void login_shouldSucceed_whenCredentialsAreCorrect() {
        LoginRequest request = new LoginRequest();
        request.setEmail("ahmet@example.com");
        request.setPassword("plain-password");

        User user = new User();
        user.setId(1L);
        user.setEmail("ahmet@example.com");
        user.setPassword("hashed-password");
        user.setCustomer(customer);

        when(userRepository.findByEmail("ahmet@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plain-password", "hashed-password")).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyLong(), anyLong())).thenReturn("fake-jwt-token");

        AuthResponse response = authService.login(request);

        assertThat(response.getToken()).isEqualTo("fake-jwt-token");
        assertThat(response.getMessage()).isEqualTo("Login successful");
    }

    @Test
    void login_shouldThrow_whenPasswordIsWrong() {
        LoginRequest request = new LoginRequest();
        request.setEmail("ahmet@example.com");
        request.setPassword("wrong-password");

        User user = new User();
        user.setEmail("ahmet@example.com");
        user.setPassword("hashed-password");

        when(userRepository.findByEmail("ahmet@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", "hashed-password")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void login_shouldThrow_whenEmailNotFound() {
        LoginRequest request = new LoginRequest();
        request.setEmail("olmayan@example.com");
        request.setPassword("123456");

        when(userRepository.findByEmail("olmayan@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}