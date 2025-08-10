package org._p1m.foodorderingsystem.features.processOrder.dto.response;

import lombok.Getter;
import lombok.Setter;
import org._p1m.foodorderingsystem.common.constant.OrderStatus;
import org._p1m.foodorderingsystem.features.users.dto.response.UserResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponseDTO {

   private Long id;
   private OrderStatus orderStatus;
   private BigDecimal totalPrice;
   private LocalDateTime orderDate;
   private String deliveryAddress;
   private UserResponseDto customer;
   private RestaurantResponseDTO restaurant;
   private List<OrderItemResponseDTO> items;
}
