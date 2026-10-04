package com.ecommerce.user.service;

import com.ecommerce.user.dto.UserDtos.*;
import com.ecommerce.user.entity.Address;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.exception.ApiExceptions.AddressNotFoundException;
import com.ecommerce.user.repository.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class AddressService {
    private final AddressRepository addresses; private final UserService userService;
    public AddressService(AddressRepository addresses, UserService userService){this.addresses=addresses;this.userService=userService;}
    @Transactional public AddressResponse create(UUID userId, AddressRequest r){
        User u=userService.getEntity(userId);
        if(r.defaultAddress() || addresses.countByUser_Id(userId)==0) clearDefaults(userId);
        Address a=toEntity(u,r); if(addresses.countByUser_Id(userId)==0) a.setDefaultAddress(true); return AddressResponse.from(addresses.save(a));
    }
    @Transactional(readOnly=true) public List<AddressResponse> list(UUID userId){userService.getEntity(userId);return addresses.findAllByUser_IdOrderByDefaultAddressDescCreatedAtDesc(userId).stream().map(AddressResponse::from).toList();}
    @Transactional public AddressResponse update(UUID userId, UUID addressId, AddressRequest r){
        userService.getEntity(userId); Address a=addresses.findByIdAndUser_Id(addressId,userId).orElseThrow(AddressNotFoundException::new);
        if(r.defaultAddress()) clearDefaults(userId); apply(a,r); return AddressResponse.from(addresses.save(a));
    }
    @Transactional public void delete(UUID userId, UUID addressId){
        userService.getEntity(userId); Address a=addresses.findByIdAndUser_Id(addressId,userId).orElseThrow(AddressNotFoundException::new); boolean wasDefault=a.isDefaultAddress(); addresses.delete(a);
        if(wasDefault) addresses.findAllByUser_IdOrderByDefaultAddressDescCreatedAtDesc(userId).stream().findFirst().ifPresent(x->{x.setDefaultAddress(true);addresses.save(x);});
    }
    @Transactional public AddressResponse setDefault(UUID userId, UUID addressId){
        userService.getEntity(userId); Address a=addresses.findByIdAndUser_Id(addressId,userId).orElseThrow(AddressNotFoundException::new); clearDefaults(userId); a.setDefaultAddress(true); return AddressResponse.from(addresses.save(a));
    }
    private void clearDefaults(UUID userId){addresses.findAllByUser_IdOrderByDefaultAddressDescCreatedAtDesc(userId).forEach(a->{if(a.isDefaultAddress()){a.setDefaultAddress(false);addresses.save(a);}});}
    private Address toEntity(User u, AddressRequest r){return Address.builder().user(u).addressType(r.addressType()).fullName(r.fullName().trim()).mobile(r.mobile().trim()).addressLine1(r.addressLine1().trim()).addressLine2(r.addressLine2()==null?null:r.addressLine2().trim()).city(r.city().trim()).state(r.state().trim()).country(r.country().trim()).postalCode(r.postalCode().trim()).defaultAddress(r.defaultAddress()).build();}
    private void apply(Address a, AddressRequest r){a.setAddressType(r.addressType());a.setFullName(r.fullName().trim());a.setMobile(r.mobile().trim());a.setAddressLine1(r.addressLine1().trim());a.setAddressLine2(r.addressLine2()==null?null:r.addressLine2().trim());a.setCity(r.city().trim());a.setState(r.state().trim());a.setCountry(r.country().trim());a.setPostalCode(r.postalCode().trim());a.setDefaultAddress(r.defaultAddress());}
}
