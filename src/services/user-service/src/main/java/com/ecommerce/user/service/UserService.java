package com.ecommerce.user.service;

import com.ecommerce.user.dto.UserDtos.*;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.exception.ApiExceptions.*;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt;
    public UserService(UserRepository users, PasswordEncoder encoder, JwtService jwt){this.users=users;this.encoder=encoder;this.jwt=jwt;}
    @Transactional public UserResponse register(RegisterRequest r){
        String email=r.email().trim().toLowerCase();
        if(users.existsByEmailIgnoreCase(email)) throw new DuplicateEmailException();
        if(users.existsByMobile(r.mobile().trim())) throw new DuplicateMobileException();
        User u=User.builder().firstName(r.firstName().trim()).lastName(r.lastName().trim()).email(email).mobile(r.mobile().trim())
                .passwordHash(encoder.encode(r.password())).status(User.UserStatus.ACTIVE).role(User.UserRole.CUSTOMER)
                .emailVerified(false).mobileVerified(false).build();
        return UserResponse.from(users.save(u));
    }
    @Transactional(readOnly=true) public LoginResponse login(LoginRequest r){
        User u=users.findByEmailIgnoreCase(r.email().trim()).orElseThrow(InvalidCredentialsException::new);
        if(u.getStatus()==User.UserStatus.BLOCKED) throw new UserBlockedException();
        if(!encoder.matches(r.password(),u.getPasswordHash())) throw new InvalidCredentialsException();
        return new LoginResponse(jwt.generate(u.getId(),u.getEmail(),u.getRole().name()),UserResponse.from(u));
    }
    @Transactional(readOnly=true) public User getEntity(UUID id){return users.findById(id).orElseThrow(UserNotFoundException::new);}
    @Transactional(readOnly=true) public UserResponse get(UUID id){return UserResponse.from(getEntity(id));}
    @Transactional public UserResponse update(UUID id, UpdateProfileRequest r){
        User u=getEntity(id);
        if(!u.getMobile().equals(r.mobile().trim()) && users.existsByMobile(r.mobile().trim())) throw new DuplicateMobileException();
        u.setFirstName(r.firstName().trim()); u.setLastName(r.lastName().trim()); u.setMobile(r.mobile().trim());
        return UserResponse.from(users.save(u));
    }
}
