package com.ecommerce.user.dto;

import com.ecommerce.user.entity.Address;
import com.ecommerce.user.entity.User;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.UUID;

public final class UserDtos {
    private UserDtos() {}
    public record RegisterRequest(@NotBlank @Size(max=80) String firstName,
                                  @NotBlank @Size(max=80) String lastName,
                                  @NotBlank @Email @Size(max=160) String email,
                                  @NotBlank @Pattern(regexp="^[0-9]{10,15}$", message="Mobile must contain 10 to 15 digits") String mobile,
                                  @NotBlank @Size(min=8, max=100) String password) {}
    public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}
    public record UpdateProfileRequest(@NotBlank @Size(max=80) String firstName,
                                       @NotBlank @Size(max=80) String lastName,
                                       @NotBlank @Pattern(regexp="^[0-9]{10,15}$", message="Mobile must contain 10 to 15 digits") String mobile) {}
    public record UserResponse(UUID id, String firstName, String lastName, String email, String mobile,
                               User.UserStatus status, User.UserRole role, boolean emailVerified,
                               boolean mobileVerified, Instant createdAt) {
        public static UserResponse from(User u) { return new UserResponse(u.getId(),u.getFirstName(),u.getLastName(),u.getEmail(),u.getMobile(),u.getStatus(),u.getRole(),u.isEmailVerified(),u.isMobileVerified(),u.getCreatedAt()); }
    }
    public record LoginResponse(String token, UserResponse user) {}
    public record AddressRequest(@NotNull Address.AddressType addressType,
                                 @NotBlank @Size(max=160) String fullName,
                                 @NotBlank @Pattern(regexp="^[0-9]{10,15}$", message="Mobile must contain 10 to 15 digits") String mobile,
                                 @NotBlank @Size(max=200) String addressLine1,
                                 @Size(max=200) String addressLine2,
                                 @NotBlank @Size(max=80) String city,
                                 @NotBlank @Size(max=80) String state,
                                 @NotBlank @Size(max=80) String country,
                                 @NotBlank @Pattern(regexp="^[A-Za-z0-9 -]{3,12}$", message="Invalid postal code") String postalCode,
                                 boolean defaultAddress) {}
    public record AddressResponse(UUID id, UUID userId, Address.AddressType addressType, String fullName, String mobile,
                                  String addressLine1, String addressLine2, String city, String state, String country,
                                  String postalCode, boolean defaultAddress, Instant createdAt) {
        public static AddressResponse from(Address a) { return new AddressResponse(a.getId(),a.getUser().getId(),a.getAddressType(),a.getFullName(),a.getMobile(),a.getAddressLine1(),a.getAddressLine2(),a.getCity(),a.getState(),a.getCountry(),a.getPostalCode(),a.isDefaultAddress(),a.getCreatedAt()); }
    }
}
