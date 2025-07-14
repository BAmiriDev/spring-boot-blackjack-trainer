package com.example.spring_boot_blackjack_trainer.controller;

import com.example.spring_boot_blackjack_trainer.model.UserProfile;
import com.example.spring_boot_blackjack_trainer.service.UserProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserProfileController.class)
@Import(UserProfileControllerTest.MockConfig.class)
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserProfileService userService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public UserProfileService userService() {
            return Mockito.mock(UserProfileService.class);
        }
    }

    @Test
    void testCreateUser() throws Exception {
        UserProfile user = new UserProfile("testUser");
        when(userService.createUser(anyString())).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .param("username", "testUser"))
                .andExpect(status().isOk());

        verify(userService, times(1)).createUser("testUser");
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(new UserProfile("testUser")));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUser() throws Exception {
        UserProfile user = new UserProfile("testUser");
        when(userService.getUserById(anyLong())).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(1L);
    }
}
