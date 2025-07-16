package org._p1m.foodorderingsystem.features.address.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.common.constant.Status;
import org._p1m.foodorderingsystem.config.exceptions.EntityNotFoundException;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.address.dto.request.AddressCreateRequestDto;
import org._p1m.foodorderingsystem.features.address.dto.request.AddressUpdateRequestDto;
import org._p1m.foodorderingsystem.features.address.dto.response.AddressDetailResponseDto;
import org._p1m.foodorderingsystem.features.address.repository.AddressRepository;
import org._p1m.foodorderingsystem.features.address.service.AddressService;
import org._p1m.foodorderingsystem.model.Address;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AddressServieImpl implements AddressService {


    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public ApiResponse createAddress(AddressCreateRequestDto createRequest) {

        Address address = new Address();
        address.setRegion(createRequest.getRegion());
        address.setCity(createRequest.getCity());
        address.setTownship(createRequest.getTownship());
        address.setRoad(createRequest.getRoad());
        address.setStreet(createRequest.getStreet());
        address.setLat(createRequest.getLat());
        address.setLongitude(createRequest.getLongitude());
        address.setEntityType(createRequest.getEntityType());
        address.setEntityId(createRequest.getEntityId());

        addressRepository.save(address);



        return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
                .data(address)
                .message("Address created successfully").build();
    }

    @Override
    public ApiResponse getAddress(Long id) {
        Address address = this.addressRepository.findByIdAndStatus(id,Status.ACTIVE)
                .orElseThrow(()-> new EntityNotFoundException("Address did not found with id: " + id));

        AddressDetailResponseDto dto = modelMapper.map(address, AddressDetailResponseDto.class);
        dto.setAddressId(address.getId());
        return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
                .data(Map.of("addressDetail", dto))
                .message("Address Detail")
                .build();
    }

    @Transactional
    public ApiResponse updateAddress(Long id, AddressUpdateRequestDto updateRequest) {

        Address address = this.addressRepository.findByIdAndStatus(id,Status.ACTIVE)
                        .orElseThrow(()-> new EntityNotFoundException("Address did not found with id: " + id));

        address.setId(id);
        address.setRegion(updateRequest.getRegion());
        address.setCity(updateRequest.getCity());
        address.setTownship(updateRequest.getTownship());
        address.setStreet(updateRequest.getStreet());
        address.setRoad(updateRequest.getRoad());
        address.setLat(updateRequest.getLat());
        address.setLongitude(updateRequest.getLongitude());
        address.setEntityType(updateRequest.getEntityType());
        address.setEntityId(updateRequest.getEntityId());

        addressRepository.save(address);
        AddressUpdateRequestDto updateRequestDto = modelMapper.map(address, AddressUpdateRequestDto.class);
        return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
                .data(Map.of("Update Address", updateRequestDto))
                .message("Address updated successfully")
                .build();
    }

    @Override
    public ApiResponse deleteAddress(Long id) {
        Address address = this.addressRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(()->new EntityNotFoundException("Address not found with id"+id));
        address.setStatus(Status.INACTIVE);
        this.addressRepository.save(address);
        return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
                .data(address)
                .message("Address deleted successfully with id: "+id)
                .build();
    }

}
