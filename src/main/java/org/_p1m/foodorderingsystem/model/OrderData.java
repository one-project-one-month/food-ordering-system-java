package org._p1m.foodorderingsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org._p1m.foodorderingsystem.common.constant.DeliveryStatus;
import org._p1m.foodorderingsystem.common.converter.DeliveryStatusConverter;
import org._p1m.foodorderingsystem.common.entity.MasterData;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderData extends MasterData {

    @Column(name = "order_date_time", nullable = false)
    private LocalDateTime orderDateTime;

    @Lob
    @Column(name = "user_address", nullable = false)
    private String userAddress;

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal totalAmount;

    @Column(name = "delivery_status", nullable = false)
    @Convert(converter = DeliveryStatusConverter.class)
    private DeliveryStatus deliveryStatus = DeliveryStatus.PENDING;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id", referencedColumnName = "id", nullable = false)
    private PaymentData payment;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private DeliveryData deliveryData;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private AddCartData addCartData;

    public OrderData() {}

    public OrderData(final LocalDateTime orderDateTime, final String userAddress, final BigDecimal totalAmount, final PaymentData payment, final DeliveryData deliveryData) {
        this.orderDateTime = orderDateTime;
        this.userAddress = userAddress;
        this.totalAmount = totalAmount;
        this.payment = payment;
        this.setDeliveryData(deliveryData);
    }

    public void setDeliveryData(DeliveryData deliveryData) {
        if (deliveryData != null) {
            deliveryData.setOrder(this);
        }
        this.deliveryData = deliveryData;
    }
}
