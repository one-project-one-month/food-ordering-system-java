package org._p1m.foodorderingsystem.features.address.service;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.dto.PaginatedApiResponse;
import org._p1m.foodorderingsystem.features.address.dto.request.AddressCreateRequestDto;
import org._p1m.foodorderingsystem.features.address.dto.request.AddressUpdateRequestDto;
import org._p1m.foodorderingsystem.features.address.dto.response.AddressDetailResponseDto;
import org.springframework.data.domain.Pageable;


public interface AddressService {
    public ApiResponse createAddress(AddressCreateRequestDto createRequest);
    public ApiResponse getAddress(Long id);
    public ApiResponse updateAddress(Long id, AddressUpdateRequestDto updateRequest);
    public ApiResponse deleteAddress(Long id);
    public PaginatedApiResponse<AddressDetailResponseDto> getAddressesDetail(Pageable pageable,Long userId);

}
