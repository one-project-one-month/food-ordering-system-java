package org._p1m.foodorderingsystem.features.processOrder.service.impl;

import org._p1m.foodorderingsystem.common.constant.OrderStatus;
import org._p1m.foodorderingsystem.config.exceptions.BadRequestException;
import org._p1m.foodorderingsystem.config.exceptions.EntityNotFoundException;
import org._p1m.foodorderingsystem.features.processOrder.dto.request.ProcessOrderRequest;
import org._p1m.foodorderingsystem.features.processOrder.dto.request.UpdateOrderStatusRequestDTO;
import org._p1m.foodorderingsystem.features.processOrder.dto.response.OrderResponseDTO;
import org._p1m.foodorderingsystem.features.processOrder.repository.DeliveryDataRepository;
import org._p1m.foodorderingsystem.features.processOrder.repository.OrderRepository;
import org._p1m.foodorderingsystem.features.processOrder.service.ProcessOrderService;
import org._p1m.foodorderingsystem.features.restaurant.repository.RestaurantRepository;
import org._p1m.foodorderingsystem.features.restaurant_vendors.repository.RestaurantVendorRepository;
import org._p1m.foodorderingsystem.features.users.repository.UserRepository;
import org._p1m.foodorderingsystem.model.DeliveryData;
import org._p1m.foodorderingsystem.model.OrderData;
import org._p1m.foodorderingsystem.model.Restaurant;
import org._p1m.foodorderingsystem.model.RestaurantVendor;
import org._p1m.foodorderingsystem.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProcessOrderServiceImpl implements ProcessOrderService {
    private final OrderRepository orderRepository;
    private final DeliveryDataRepository deliveryDataRepository;
    private final RestaurantVendorRepository restaurantVendorRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    public ProcessOrderServiceImpl(OrderRepository orderRepository, DeliveryDataRepository deliveryDataRepository, RestaurantVendorRepository restaurantVendorRepository, UserRepository userRepository, RestaurantRepository restaurantRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.deliveryDataRepository = deliveryDataRepository;
        this.restaurantVendorRepository = restaurantVendorRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public OrderResponseDTO updateOrderStatus(Long orderId, UpdateOrderStatusRequestDTO request) {
        OrderData order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id:" + orderId));

        OrderStatus newStatus = request.getOrderStatus();
        if (newStatus != OrderStatus.APPROVED && newStatus != OrderStatus.REJECTED) {
            throw new BadRequestException("Action invalid. Status can only be updated to APPROVED or REJECTED.");
        }

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("Order cannot be processed. It is not in PENDING state.");
        }

        order.setOrderStatus(newStatus);
        OrderData updateOrder = orderRepository.save(order);

        return modelMapper.map(updateOrder, OrderResponseDTO.class);
    }

    @Override
    @Transactional
    public OrderResponseDTO processOrder(ProcessOrderRequest request) {
        // Fetch the order from the database using the ID from the request
        OrderData orderData = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + request.getOrderId()));

        // Fetch other managed entities from the database
        User orderingUser = userRepository.findById(orderData.getPayment().getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("Ordering user not found with id: " + orderData.getPayment().getUser().getId()));

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + request.getRestaurantId()));

        // Set managed entities to ensure correct foreign key mapping
        orderData.getPayment().setUser(orderingUser);
        orderData.getPayment().setRestaurant(restaurant);

        // Save the order
        OrderData savedOrder = orderRepository.save(orderData);

        User deliveryStaff = userRepository.findById(request.getDeliveryStaffId())
                .orElseThrow(() -> new EntityNotFoundException("Delivery staff not found with id: " + request.getDeliveryStaffId()));

        DeliveryData deliveryData = new DeliveryData(savedOrder, deliveryStaff);
        deliveryDataRepository.save(deliveryData);

        RestaurantVendor restaurantVendor = new RestaurantVendor();
        restaurantVendor.setRestaurant(restaurant);
        restaurantVendor.setDeliveryUser(deliveryStaff);
        restaurantVendorRepository.save(restaurantVendor);

        return modelMapper.map(savedOrder, OrderResponseDTO.class);
    }
}
