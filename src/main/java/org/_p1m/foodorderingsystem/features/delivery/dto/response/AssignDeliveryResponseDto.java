package org._p1m.foodorderingsystem.features.delivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org._p1m.foodorderingsystem.common.constant.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignDeliveryResponseDto {
    private Long orderId;
    private Long deliveryStaffId;
    private Status deliveryStaffStatus;
}
