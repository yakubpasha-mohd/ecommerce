package com.ecommerce.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="addresses")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Address {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="user_id", nullable=false, foreignKey=@ForeignKey(name="fk_addresses_user"))
    private User user;
    @Enumerated(EnumType.STRING) @Column(name="address_type", nullable=false, length=20) private AddressType addressType;
    @Column(name="full_name", nullable=false, length=160) private String fullName;
    @Column(nullable=false, length=20) private String mobile;
    @Column(name="address_line1", nullable=false, length=200) private String addressLine1;
    @Column(name="address_line2", length=200) private String addressLine2;
    @Column(nullable=false, length=80) private String city;
    @Column(nullable=false, length=80) private String state;
    @Column(nullable=false, length=80) private String country;
    @Column(name="postal_code", nullable=false, length=12) private String postalCode;
    @Column(name="is_default", nullable=false) private boolean defaultAddress;
    @Column(name="created_at", nullable=false) private Instant createdAt;
    @Column(name="updated_at", nullable=false) private Instant updatedAt;
    @PrePersist void onCreate(){ createdAt=Instant.now(); updatedAt=createdAt; }
    @PreUpdate void onUpdate(){ updatedAt=Instant.now(); }
    public enum AddressType { HOME, WORK, OTHER }
}
