package com.ecommerce.user.controller;

import com.ecommerce.user.dto.*;
import com.ecommerce.user.dto.UserDtos.*;
import com.ecommerce.user.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/users")
public class AddressController {
    private final AddressService service; public AddressController(AddressService service){this.service=service;}
    @PostMapping("/{userId}/addresses") @ResponseStatus(HttpStatus.CREATED) public ApiResponse<AddressResponse> create(@PathVariable UUID userId,@Valid @RequestBody AddressRequest r,Authentication a){ensureSelf(userId,a);return ApiResponse.ok(service.create(userId,r),"Address created successfully");}
    @GetMapping("/{userId}/addresses") public ApiResponse<List<AddressResponse>> list(@PathVariable UUID userId,Authentication a){ensureSelf(userId,a);return ApiResponse.ok(service.list(userId),"Addresses loaded");}
    @PutMapping("/{userId}/addresses/{addressId}") public ApiResponse<AddressResponse> update(@PathVariable UUID userId,@PathVariable UUID addressId,@Valid @RequestBody AddressRequest r,Authentication a){ensureSelf(userId,a);return ApiResponse.ok(service.update(userId,addressId,r),"Address updated");}
    @DeleteMapping("/{userId}/addresses/{addressId}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable UUID userId,@PathVariable UUID addressId,Authentication a){ensureSelf(userId,a);service.delete(userId,addressId);}
    @PutMapping("/{userId}/addresses/{addressId}/default") public ApiResponse<AddressResponse> setDefault(@PathVariable UUID userId,@PathVariable UUID addressId,Authentication a){ensureSelf(userId,a);return ApiResponse.ok(service.setDefault(userId,addressId),"Default address updated");}
    @PostMapping("/me/addresses") @ResponseStatus(HttpStatus.CREATED) public ApiResponse<AddressResponse> createMe(@Valid @RequestBody AddressRequest r, Authentication a){UUID userId=currentUser(a);return ApiResponse.ok(service.create(userId,r),"Address created successfully");}
    @GetMapping("/me/addresses") public ApiResponse<List<AddressResponse>> listMe(Authentication a){UUID userId=currentUser(a);return ApiResponse.ok(service.list(userId),"Addresses loaded");}
    @PutMapping("/me/addresses/{addressId}") public ApiResponse<AddressResponse> updateMe(@PathVariable UUID addressId,@Valid @RequestBody AddressRequest r,Authentication a){return ApiResponse.ok(service.update(currentUser(a),addressId,r),"Address updated");}
    @DeleteMapping("/me/addresses/{addressId}") @ResponseStatus(HttpStatus.NO_CONTENT) public void deleteMe(@PathVariable UUID addressId,Authentication a){service.delete(currentUser(a),addressId);}
    @PutMapping("/me/addresses/{addressId}/default") public ApiResponse<AddressResponse> setDefaultMe(@PathVariable UUID addressId,Authentication a){return ApiResponse.ok(service.setDefault(currentUser(a),addressId),"Default address updated");}
    private UUID currentUser(Authentication a){return UUID.fromString(a.getName());}
    private void ensureSelf(UUID id,Authentication a){if(!id.toString().equals(a.getName())) throw new org.springframework.security.access.AccessDeniedException("You can only access your own addresses");}
}
