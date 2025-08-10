package org._p1m.foodorderingsystem.features.address.controller;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.dto.PaginatedApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.address.dto.request.AddressCreateRequestDto;
import org._p1m.foodorderingsystem.features.address.dto.request.AddressUpdateRequestDto;
import org._p1m.foodorderingsystem.features.address.dto.response.AddressDetailResponseDto;
import org._p1m.foodorderingsystem.features.address.service.AddressService;
import org._p1m.foodorderingsystem.features.menu.dto.responses.MenuResponseDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.base.path}/auth/address")
@RequiredArgsConstructor
@Tag(name = "Address API", description = "Endpoints for managing address")
public class AddressController {
        private final AddressService addressService;
        @PostMapping
        @Operation(
                summary = "Create a new address",
                description = "Registers a new address in the system.",
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                        description = "Address creation request",
                        required = true,
                        content = @Content(schema = @Schema(implementation = AddressCreateRequestDto.class))
                ),
                responses = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Address created successfully"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
                }
        )
        public ResponseEntity<ApiResponse> createAddress(
                @Valid @RequestBody final AddressCreateRequestDto addressCreateRequest,
                final HttpServletRequest request
        ){
                final ApiResponse response = this.addressService.createAddress(addressCreateRequest);
                return ResponseUtils.buildResponse(request,response);
        }
        @GetMapping("/{id}")
        @Operation(
                summary = "Get Address detail",
                description = "Get Address detail by its ID.",
                parameters = {
                        @Parameter(name = "id", description = "Address ID", required = true)
                },
                responses = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Address details retrieved successfully"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Address not found")
                }
        )
        public ResponseEntity<ApiResponse> addressDetail(@PathVariable(name="id") Long id, HttpServletRequest request) {
                final ApiResponse response = this.addressService.getAddress(id);
                return ResponseUtils.buildResponse(request, response);
        }
        
        @GetMapping("/getAll/{userId}")
        @Operation(
                summary = "Get all Addresses by user id.",
                description = "Get Addresses detail by its user ID.",
                responses = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Fetched successfully"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Addresses not found")
                }
        )
        public ResponseEntity<PaginatedApiResponse<AddressDetailResponseDto>> addressesDetails(
        		@Parameter(description = "Page number") 
        		@RequestParam(value = "page", defaultValue = "0") int page,
        		@Parameter(description = "Page size")
        		@RequestParam(value = "size", defaultValue = "20") int size,
        		@PathVariable(name="userId") Long userId, HttpServletRequest request) {
        	Pageable pageable = PageRequest.of(page, size);
        	final PaginatedApiResponse<AddressDetailResponseDto> response = this.addressService.getAddressesDetail(pageable,userId);
                return ResponseUtils.buildPaginatedResponse(request, response);
        }
        
        @PutMapping("/{id}")
        @Operation(
                summary = "Update a address",
                description = "Updates the details of an existing address.",
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                        description = "Address update request",
                        required = true,
                        content = @Content(schema = @Schema(implementation = AddressUpdateRequestDto.class))
                ),
                parameters = {
                        @Parameter(name = "id", description = "Address ID", required = true)
                },
                responses = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Address updated successfully"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Address not found")
                }
        )
        public ResponseEntity<ApiResponse> updateAddress(@Valid @RequestBody AddressUpdateRequestDto addressUpdateRequestDto,
                                                         @PathVariable(name="id") Long id,
                                                         HttpServletRequest request) {
                final ApiResponse response = this.addressService.updateAddress(id, addressUpdateRequestDto);
                return ResponseUtils.buildResponse(request, response);

        }

        @DeleteMapping("/{id}")
        @Operation(
                summary = "Delete a address",
                description = "Deletes a address from the system by its ID.",
                parameters = {
                        @Parameter(name = "id", description = "Address ID", required = true)
                },
                responses = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Address deleted successfully"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Address not found")
                }
        )
        public ResponseEntity<ApiResponse> deleteAddress(@PathVariable(name="id") Long id, HttpServletRequest request) {
                final ApiResponse response = this.addressService.deleteAddress(id);
                return ResponseUtils.buildResponse(request, response);
        }
}