package com.example.spring_boot_blackjack_trainer.service;

import com.example.spring_boot_blackjack_trainer.model.UserProfile;
import com.example.spring_boot_blackjack_trainer.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class UserProfileServiceImplTest {

    @Mock
    private UserProfileRepository userRepo;

    @InjectMocks
    private UserProfileServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser() {
        UserProfile user = new UserProfile("saveTest");

        userService.saveUser(user);

        verify(userRepo, times(1)).save(user);
    }

    @Test
    void testCreateUser() {
        UserProfile user = new UserProfile("newUser");
        when(userRepo.save(any(UserProfile.class))).thenReturn(user);

        UserProfile created = userService.createUser("newUser");

        assertNotNull(created);
        assertEquals("newUser", created.getUsername());
        verify(userRepo, times(1)).save(any(UserProfile.class));
    }

    @Test
    void testGetAllUsers() {
        UserProfile user1 = new UserProfile("user1");
        UserProfile user2 = new UserProfile("user2");

        when(userRepo.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<UserProfile> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepo, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        UserProfile user = new UserProfile("findMe");
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        UserProfile found = userService.getUserById(1L);

        assertNotNull(found);
        assertEquals("findMe", found.getUsername());
        verify(userRepo, times(1)).findById(1L);
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(1L);

        verify(userRepo, times(1)).deleteById(1L);
    }
}
