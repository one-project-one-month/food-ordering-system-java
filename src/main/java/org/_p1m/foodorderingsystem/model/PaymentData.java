package org._p1m.foodorderingsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org._p1m.foodorderingsystem.common.constant.Status;
import org._p1m.foodorderingsystem.common.entity.MasterData;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class PaymentData extends MasterData {

    @Column(name = "payment_screenshot")
    private String paymentScreenshot;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deliver_id")
    private User deliveryUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "res_id", nullable = false)
    private Restaurant restaurant;

    @OneToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

//    @OneToOne
//    @JoinColumn(name = "rating_id")
//    private Rating rating;

    @OneToOne(mappedBy = "payment")
    private OrderData order;

    public PaymentData() {}
}
