package com.ekim.bankingapi.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class CustomerControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void createCustomer_shouldReturn201_whenDataIsValid() throws Exception {
        CustomerRequest request = new CustomerRequest();
        request.setFirstName("Test");
        request.setLastName("User");
        request.setEmail("integration-test-" + System.currentTimeMillis() + "@example.com");
        request.setPhoneNumber("05550000000");
        request.setNationalId(String.valueOf(System.currentTimeMillis()).substring(0, 11));
        request.setAge(30);
        request.setAddress("Test Address");

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.email").value(request.getEmail()));
    }

    @Test
    void createCustomer_shouldReturn400_whenEmailIsMissing() throws Exception {
        CustomerRequest request = new CustomerRequest();
        request.setFirstName("Test");
        request.setLastName("User");
        request.setEmail("");
        request.setPhoneNumber("05550000000");
        request.setNationalId("99999999999");
        request.setAge(30);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }

    @Test
    void getCustomerById_shouldReturn404_whenNotFound() throws Exception {
        mockMvc.perform(get("/api/customers/999999"))
                .andExpect(status().isNotFound());
    }
}