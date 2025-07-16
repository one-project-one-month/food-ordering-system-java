package org._p1m.foodorderingsystem.features.processOrder.service.impl;

import org._p1m.foodorderingsystem.common.constant.OrderStatus;
import org._p1m.foodorderingsystem.config.exceptions.BadRequestException;
import org._p1m.foodorderingsystem.config.exceptions.EntityNotFoundException;
import org._p1m.foodorderingsystem.features.processOrder.dto.request.UpdateOrderStatusRequestDTO;
import org._p1m.foodorderingsystem.features.processOrder.dto.response.OrderResponseDTO;
import org._p1m.foodorderingsystem.features.processOrder.repository.OrderRepository;
import org._p1m.foodorderingsystem.features.processOrder.service.OrderService;
import org._p1m.foodorderingsystem.model.OrderData;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper){
        this.orderRepository = orderRepository;
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
}
