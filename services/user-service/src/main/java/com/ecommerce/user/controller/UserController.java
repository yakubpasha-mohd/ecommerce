package com.ecommerce.user.controller;

import com.ecommerce.user.dto.*;
import com.ecommerce.user.dto.UserDtos.*;
import com.ecommerce.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService service; public UserController(UserService service){this.service=service;}
    @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED) public ApiResponse<UserResponse> register(@Valid @RequestBody RegisterRequest r){return ApiResponse.ok(service.register(r),"User created successfully");}
    @PostMapping("/login") public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest r){return ApiResponse.ok(service.login(r),"Login successful");}
    @GetMapping("/me") public ApiResponse<UserResponse> me(Authentication a){return ApiResponse.ok(service.get(UUID.fromString(a.getName())),"Profile loaded");}
    @PutMapping("/me") public ApiResponse<UserResponse> updateMe(Authentication a,@Valid @RequestBody UpdateProfileRequest r){return ApiResponse.ok(service.update(UUID.fromString(a.getName()),r),"Profile updated");}
    @GetMapping("/{userId}") public ApiResponse<UserResponse> get(@PathVariable UUID userId,Authentication a){ensureSelf(userId,a);return ApiResponse.ok(service.get(userId),"Profile loaded");}
    @PutMapping("/{userId}") public ApiResponse<UserResponse> update(@PathVariable UUID userId,@Valid @RequestBody UpdateProfileRequest r,Authentication a){ensureSelf(userId,a);return ApiResponse.ok(service.update(userId,r),"Profile updated");}
    private void ensureSelf(UUID id,Authentication a){if(!id.toString().equals(a.getName())) throw new org.springframework.security.access.AccessDeniedException("You can only access your own profile");}
}
