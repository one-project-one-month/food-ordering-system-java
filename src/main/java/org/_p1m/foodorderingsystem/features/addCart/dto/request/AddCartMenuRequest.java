package org._p1m.foodorderingsystem.features.addCart.dto.request;

import lombok.Data;
import org._p1m.foodorderingsystem.model.DishSize;
import org._p1m.foodorderingsystem.model.Extra;
import org._p1m.foodorderingsystem.model.OrderData;
import org._p1m.foodorderingsystem.model.User;

@Data
public class AddCartMenuRequest {

    private Integer quantity;

    private User customer;

    private DishSize dishSize;

    private Extra extra;

    private OrderData orderData;

}
