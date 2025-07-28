package org._p1m.foodorderingsystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org._p1m.foodorderingsystem.common.constant.DeliveryStatus;
import org._p1m.foodorderingsystem.common.constant.OrderStatus;
import org._p1m.foodorderingsystem.common.converter.DeliveryStatusConverter;
import org._p1m.foodorderingsystem.common.converter.OrderStatusConverter;
import org._p1m.foodorderingsystem.common.entity.MasterData;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderData extends MasterData {

    @Column(name = "order_date_time", nullable = false)
    private LocalDateTime orderDateTime;

//    @Lob
//    @Column(name = "user_address", nullable = false)
//    private String userAddress;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address userAddress;
    
    @Column(name = "total_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal totalAmount;

    @Column(name = "order_status", nullable = false)
    @Convert(converter = OrderStatusConverter.class)
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Column(name = "delivery_status", nullable = false)
    @Convert(converter = DeliveryStatusConverter.class)
    private DeliveryStatus deliveryStatus = DeliveryStatus.PENDING;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id", referencedColumnName = "id", nullable = false)
    private PaymentData payment;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private DeliveryData deliveryData;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddCartData> addCartItems = new ArrayList<>();

    public OrderData() {}

    public OrderData(final LocalDateTime orderDateTime, final Address userAddress, final BigDecimal totalAmount, final PaymentData payment, final DeliveryData deliveryData) {
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
    public void addCartItem(AddCartData cartItem) {
    	if(cartItem != null) {
    		cartItem.setOrder(this);
    	}
        this.addCartItems.add(cartItem);
    }
}
