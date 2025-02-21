package com.equifax.users.serviceImpl;

import com.equifax.users.dto.UserLoginDTO;
import com.equifax.users.entities.User;
import com.equifax.users.exceptions.NotFoundException;
import com.equifax.users.repositories.UserRepository;
import com.equifax.users.utils.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    /**
     * Tests the login functionality with valid email and password credentials.
     *
     * Verifies that a user can successfully log in when the provided credentials
     * match existing records in the database. Ensures the generated JWT token is included
     * in the returned User object. Mocks the required repository and utility calls for
     * password validation, JWT token generation, and saving the user entity.
     *
     * Asserts:
     * - The returned User object is not null.
     * - The generated JWT token within the returned User object is not null.
     *
     * Mocked Interactions:
     * - Finds a user by email in the repository.
     * - Verifies the password using a password encoder.
     * - Generates a JWT token for the user.
     * - Saves the authenticated user entity.
     */
    @Test
    void testLogin_ValidCredentials() {
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setEmail("johndoe@example.com");
        loginDTO.setPassword("password123");

        User user = User.builder()
                .email("johndoe@example.com")
                .password("encodedPassword")
                .build();

        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtTokenUtil.generateToken("johndoe@example.com")).thenReturn("jwtToken");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.login(loginDTO);

        assertNotNull(result);
        assertNotNull(result.getToken());
    }

    /**
     * Tests the login functionality of the UserService when an invalid password is provided.
     *
     * This test verifies that:
     * - The {@code findByEmail} method of {@code UserRepository} is called to fetch the user by email.
     * - The provided password does not match the stored password.
     * - A {@link NotFoundException} is thrown with the message "Invalid email or password".
     * - The {@code save} method of {@code UserRepository} is not called.
     *
     * Scenarios covered:
     * - The email exists in the database, but the provided password is incorrect.
     *
     * Expected behavior:
     * - The login attempt fails, and the system does not persist any user entity.
     */
    @Test
    void testLogin_InvalidPassword() {
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setEmail("johndoe@example.com");
        loginDTO.setPassword("wrongPassword");

        User user = User.builder()
                .email("johndoe@example.com")
                .password("encodedPassword")
                .build();

        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.login(loginDTO));

        assertEquals("Invalid email or password", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    /**
     * Verifies the behavior of the login functionality when the provided email is not found in the database.
     *
     * This test ensures that if the email specified in the UserLoginDTO object does not exist within the user repository,
     * the method throws a NotFoundException with an appropriate error message ("Invalid email or password")
     * and does not attempt to save any user data in the repository.
     *
     * Test Procedure:
     * - Create a UserLoginDTO object with an email value that does not exist (e.g., "nonexistent@example.com").
     * - Mock the userRepository's `findByEmail` method to return an empty Optional for the given email.
     * - Verify that invoking the `login` method of `userService` results in a NotFoundException being thrown.
     * - Confirm that the exception message matches the expected error text.
     * - Ensure that no calls are made to save a user in the repository.
     */
    @Test
    void testLogin_EmailNotFound() {
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setEmail("nonexistent@example.com");
        loginDTO.setPassword("password123");

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(java.util.Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.login(loginDTO));

        assertEquals("Invalid email or password", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}