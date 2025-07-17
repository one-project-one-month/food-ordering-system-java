package org._p1m.foodorderingsystem.features.delivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org._p1m.foodorderingsystem.common.constant.Status;

@Data
@AllArgsConstructor
public class GetAllVendorsResponseDto {
    private Long deliveryStaffId;
    private Status deliveryStatus;


}
