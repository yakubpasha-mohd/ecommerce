package com.ecommerce.user.service;

import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    @Mock UserRepository users; @Mock PasswordEncoder encoder; @Mock JwtService jwt;
    @InjectMocks UserService service;
    @Test void registerHashesPasswordAndCreatesCustomer(){
        when(users.existsByEmailIgnoreCase(anyString())).thenReturn(false); when(users.existsByMobile(anyString())).thenReturn(false);
        when(encoder.encode("Password@123")).thenReturn("hash");
        when(users.save(any(User.class))).thenAnswer(i->{User u=i.getArgument(0);u.setId(UUID.randomUUID());return u;});
        var out=service.register(new com.ecommerce.user.dto.UserDtos.RegisterRequest("Test","User","test@example.com","9876543210","Password@123"));
        assertNotNull(out.id()); assertEquals("hash", users.findById(out.id()).orElseThrow().getPasswordHash()); verify(encoder).encode("Password@123");
    }
}
