package org._p1m.foodorderingsystem.features.processOrder.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org._p1m.foodorderingsystem.common.constant.OrderStatus;

@Getter
@Setter
public class UpdateOrderStatusRequestDTO {
    @NotNull(message = "Status cannot be null")
    private OrderStatus orderStatus;
}
