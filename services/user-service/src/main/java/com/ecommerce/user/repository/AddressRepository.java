package com.ecommerce.user.repository;

import com.ecommerce.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    List<Address> findAllByUser_IdOrderByDefaultAddressDescCreatedAtDesc(UUID userId);
    Optional<Address> findByIdAndUser_Id(UUID id, UUID userId);
    long countByUser_Id(UUID userId);
}
