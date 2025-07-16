package org._p1m.foodorderingsystem.features.address.service;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.address.dto.request.AddressCreateRequestDto;
import org._p1m.foodorderingsystem.features.address.dto.request.AddressUpdateRequestDto;


public interface AddressService {
    public ApiResponse createAddress(AddressCreateRequestDto createRequest);
    public ApiResponse getAddress(Long id);
    public ApiResponse updateAddress(Long id, AddressUpdateRequestDto updateRequest);
    public ApiResponse deleteAddress(Long id);
}
