package com.ecommerce.user.service;

import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository users;

    @Mock
    PasswordEncoder encoder;

    @Mock
    JwtService jwt;

    @InjectMocks
    UserService service;

    @Test
    void registerHashesPasswordAndCreatesCustomer() {

        when(users.existsByEmailIgnoreCase(anyString()))
                .thenReturn(false);

        when(users.existsByMobile(anyString()))
                .thenReturn(false);

        when(encoder.encode("Password@123"))
                .thenReturn("hash");

        when(users.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setId(UUID.randomUUID());
                    return user;
                });

        var request =
                new com.ecommerce.user.dto.UserDtos.RegisterRequest(
                        "Test",
                        "User",
                        "test@example.com",
                        "9876543210",
                        "Password@123"
                );

        var out = service.register(request);

        assertNotNull(out);
        assertNotNull(out.id());

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(users).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertNotNull(savedUser);

        assertEquals("Test", savedUser.getFirstName());
        assertEquals("User", savedUser.getLastName());
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("9876543210", savedUser.getMobile());

        assertEquals(
                "hash",
                savedUser.getPasswordHash()
        );

        assertNotEquals(
                "Password@123",
                savedUser.getPasswordHash()
        );

        assertNotNull(savedUser.getRole());

        assertEquals(
                "CUSTOMER",
                savedUser.getRole().name()
        );

        verify(encoder).encode("Password@123");

        verify(users)
                .existsByEmailIgnoreCase("test@example.com");

        verify(users)
                .existsByMobile("9876543210");

        verify(users)
                .save(any(User.class));

        verifyNoMoreInteractions(encoder);
    }
}
