package org._p1m.foodorderingsystem.features.delivery.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org._p1m.foodorderingsystem.common.constant.DeliveryStatus;
import org._p1m.foodorderingsystem.common.constant.Status;
import org._p1m.foodorderingsystem.config.exceptions.EntityNotFoundException;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.dto.PaginatedApiResponse;
import org._p1m.foodorderingsystem.config.response.dto.PaginationMeta;
import org._p1m.foodorderingsystem.features.delivery.dto.request.ApplyDeliveryStaffRequest;
import org._p1m.foodorderingsystem.features.delivery.dto.request.AssignDeliveryRequest;
import org._p1m.foodorderingsystem.features.delivery.dto.response.ApplyDeliveryResponse;
import org._p1m.foodorderingsystem.features.delivery.dto.response.AssignDeliveryResponseDto;
import org._p1m.foodorderingsystem.features.delivery.dto.response.GetAllVendorsResponseDto;
import org._p1m.foodorderingsystem.features.delivery.service.DeliveryDataService;
import org._p1m.foodorderingsystem.features.order.dto.response.OrderResponseDto;
import org._p1m.foodorderingsystem.features.order.repository.OrderDataRepository;
import org._p1m.foodorderingsystem.features.restaurant.repository.RestaurantRepository;
import org._p1m.foodorderingsystem.features.restaurant_vendors.repository.RestaurantVendorRepository;
import org._p1m.foodorderingsystem.features.users.repository.ProfileRepository;
import org._p1m.foodorderingsystem.features.users.repository.UserRepository;
import org._p1m.foodorderingsystem.model.OrderData;
import org._p1m.foodorderingsystem.model.Profile;
import org._p1m.foodorderingsystem.model.Restaurant;
import org._p1m.foodorderingsystem.model.RestaurantVendor;
import org._p1m.foodorderingsystem.model.User;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryDataServiceImpl implements DeliveryDataService {

    private final OrderDataRepository orderDataRepository;
    private final UserRepository userRepository;
    private final RestaurantVendorRepository restaurantVendorRepository;
    private final RestaurantRepository restaruantRepository;
    private final ProfileRepository profileRepository;
    
    @Override
    @Transactional
    public ApiResponse assignDelivery(AssignDeliveryRequest assignDeliveryRequest) {

        OrderData orderData = this.orderDataRepository.findById(assignDeliveryRequest.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        User deliveryStaff = this.userRepository.findById(assignDeliveryRequest.getDeliveryId())
                .orElseThrow(() -> new EntityNotFoundException("Delivery Staff not found"));

        RestaurantVendor restaurantVendor = this.restaurantVendorRepository.findByRestaurantIdAndDeliveryUserId(assignDeliveryRequest.getRestaurantId(),
                        assignDeliveryRequest.getDeliveryId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurant Vendor not found"));

        if (restaurantVendor.getStatus() == Status.INACTIVE) {
            throw new EntityNotFoundException("Your assign delivery staff is not free.");
        }

        orderData.setDeliveryStatus(DeliveryStatus.ON_THE_WAY);
        this.orderDataRepository.save(orderData);

        this.restaurantVendorRepository.updateDeliveryStatus(assignDeliveryRequest.getRestaurantId(),
                assignDeliveryRequest.getDeliveryId(),
                Status.INACTIVE);

        AssignDeliveryResponseDto dto = new AssignDeliveryResponseDto(
                orderData.getId(),
                deliveryStaff.getId(),
                Status.INACTIVE
        );

        return ApiResponse.builder().success(1).code(HttpStatus.CREATED.value())
                .data(Map.of("Delivery Data", dto))
                .message("Assign Delivery Successfully")
                .build();
    }

    @Override
    public PaginatedApiResponse<GetAllVendorsResponseDto>  getAllDeliveryStaffData(Pageable pageable,Long restaurantId,Status status) {
    	Page<RestaurantVendor> page = this.restaurantVendorRepository.findDeliveryByRestaurantIdWithStatus(restaurantId,status,pageable);
//        List<RestaurantVendor> restaurantVendors = this.restaurantVendorRepository.findByRestaurantId(restaurantId);
        
    	if (page.isEmpty()) {
    	    throw new RuntimeException("No delivery data is found for this restaurant");
    	}
        
        List<GetAllVendorsResponseDto> data = page.getContent().stream()
                .map(vendors -> {
                	GetAllVendorsResponseDto dto = new GetAllVendorsResponseDto();
                    dto.setDeliveryStaffId(vendors.getDeliveryUser().getId());
                    dto.setDeliveryName(vendors.getDeliveryUser().getProfile().getName());
                    dto.setDeliveryStatus(vendors.getStatus());
                    return dto;
                })
                .toList();
        PaginationMeta meta = new PaginationMeta();
        meta.setTotalItems(page.getTotalElements());
        meta.setTotalPages(page.getTotalPages());
        meta.setCurrentPage(pageable.getPageNumber()+1);
        return PaginatedApiResponse.<GetAllVendorsResponseDto>builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Deliveries are fetched successfully")
                .meta(meta)
                .data(data)
                .build();
    // if we want free delivery staffs
//        List<RestaurantVendor> activeVendors = restaurantVendors.stream()
//                .filter(v -> v.getStatus() == Status.ACTIVE)
//                .toList();
//        List<GetAllVendorsResponseDto> dto = restaurantVendors.stream()
//                .map(vendor -> new GetAllVendorsResponseDto(
//                        vendor.getDeliveryUser().getId(),
//                        vendor.getDeliveryUser().getProfile().getName(),
//                        vendor.getStatus()
//                ))
//                .toList();

//        return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
//                .data(Map.of("Delivery Staff Data", dto))
//                .message("Retrieve Delivery Staff Data Successfully")
//                .build();
    }

	@Override
	public ApiResponse applyRestaurantByDeliveryStaff(ApplyDeliveryStaffRequest request) {
		Restaurant restaurant = this.restaruantRepository.findById(request.getRestaurantId())
        .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: "+request.getRestaurantId()));
		User user = this.userRepository.findById(request.getUserId())
		        .orElseThrow(() -> new EntityNotFoundException("User not found with id: "+request.getUserId()));
		Profile profile = this.profileRepository.findByUser_Id(request.getUserId())
		.orElseThrow(() -> new EntityNotFoundException("User's profile not found with id: "+request.getUserId()));
		if(profile.getCount().equals(3)) {
			return ApiResponse.builder().success(0).code(HttpStatus.OK.value())
	                .data(null)
	                .message("You can't apply more than three restaurants.")
	                .build();
		}
		boolean alreadyApplied = restaurantVendorRepository.existsByRestaurantIdAndDeliveryUserId(
			    request.getRestaurantId(), request.getUserId());

			if (alreadyApplied) {
			    return ApiResponse.builder()
			            .success(0)
			            .code(HttpStatus.OK.value())
			            .data(null)
			            .message("You have already applied to this restaurant.")
			            .build();
			}
		RestaurantVendor resVan = new RestaurantVendor();
		resVan.setRestaurant(restaurant);
		resVan.setDeliveryUser(user);
		restaurantVendorRepository.save(resVan);
		
		profile.setCount(Optional.ofNullable(profile.getCount()).orElse(0) + 1);
		profileRepository.save(profile);
		
		ApplyDeliveryResponse dto = new ApplyDeliveryResponse(
                restaurant.getId(),
                user.getId(),
                profile.getCount(),
                resVan.getStatus()
        );
		return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
                .data(dto)
                .message("Delivery staff apply Successfully")
                .build();
	}
}
