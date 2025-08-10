package org._p1m.foodorderingsystem.features.delivery.dto.response;

import org._p1m.foodorderingsystem.common.constant.Status;

import lombok.Data;

@Data
//@AllArgsConstructor
public class GetAllVendorsResponseDto {
    private Long deliveryStaffId;
    private String deliveryName;
    private Status deliveryStatus;
}
