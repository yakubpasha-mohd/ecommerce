package com.ecommerce.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_users_mobile", columnNames = "mobile")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name="first_name", nullable=false, length=80) private String firstName;
    @Column(name="last_name", nullable=false, length=80) private String lastName;
    @Column(nullable=false, length=160) private String email;
    @Column(nullable=false, length=20) private String mobile;
    @Column(name="password_hash", nullable=false, length=255) private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private UserStatus status;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=30) private UserRole role;
    @Column(name="email_verified", nullable=false) private boolean emailVerified;
    @Column(name="mobile_verified", nullable=false) private boolean mobileVerified;
    @Column(name="created_at", nullable=false) private Instant createdAt;
    @Column(name="updated_at", nullable=false) private Instant updatedAt;
    @PrePersist void onCreate(){ createdAt=Instant.now(); updatedAt=createdAt; }
    @PreUpdate void onUpdate(){ updatedAt=Instant.now(); }
    public enum UserStatus { ACTIVE, INACTIVE, BLOCKED, PENDING }
    public enum UserRole { CUSTOMER, ADMIN }
}
