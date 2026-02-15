package com.cognizant.carservice.security;

import com.cognizant.carservice.service.CarServiceService;
import com.cognizant.carservice.mapper.CarServiceMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class CarServiceSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class MockConfig {
        @Bean
        CarServiceService service() {
            return org.mockito.Mockito.mock(CarServiceService.class);
        }

        @Bean
        CarServiceMapper mapper() {
            return org.mockito.Mockito.mock(CarServiceMapper.class);
        }
    }

    // ❌ no auth
    @Test
    void shouldReturn401_whenNoToken() throws Exception {
        mockMvc.perform(get("/carservice/all"))
                .andExpect(status().isUnauthorized());
    }

    // 👤 user role allowed
    @Test
    @WithMockUser(authorities = "SCOPE_user")
    void shouldAllowUserRole() throws Exception {
        mockMvc.perform(get("/carservice/all"))
                .andExpect(status().isOk());
    }

    // 👑 admin role allowed
    @Test
    @WithMockUser(authorities = "SCOPE_admin")
    void shouldAllowAdminRole() throws Exception {
        mockMvc.perform(get("/carservice/all"))
                .andExpect(status().isOk());
    }

    // ❌ wrong role
    @Test
    @WithMockUser(authorities = "SCOPE_random")
    void shouldRejectWrongRole() throws Exception {
        mockMvc.perform(get("/carservice/all"))
                .andExpect(status().isForbidden());
    }
}