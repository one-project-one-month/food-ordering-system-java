package org._p1m.foodorderingsystem.features.payment.service.impl;

import org._p1m.foodorderingsystem.config.exceptions.EntityNotFoundException;
import org._p1m.foodorderingsystem.features.payment.dto.PaymentDTO;
import org._p1m.foodorderingsystem.features.payment.repository.PaymentRepository;
import org._p1m.foodorderingsystem.features.payment.service.PaymentService;
import org._p1m.foodorderingsystem.features.restaurant.repository.RestaurantRepository;
import org._p1m.foodorderingsystem.features.users.repository.UserRepository;
import org._p1m.foodorderingsystem.model.PaymentData;
import org._p1m.foodorderingsystem.model.Restaurant;
import org._p1m.foodorderingsystem.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    public PaymentServiceImpl(PaymentRepository paymentRepository, UserRepository userRepository, RestaurantRepository restaurantRepository, ModelMapper modelMapper) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        PaymentData paymentData = modelMapper.map(paymentDTO, PaymentData.class);

        User user = userRepository.findById(paymentDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + paymentDTO.getUserId()));
        paymentData.setUser(user);

        Restaurant restaurant = restaurantRepository.findById(paymentDTO.getResId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + paymentDTO.getResId()));
        paymentData.setRestaurant(restaurant);

        if (paymentDTO.getDeliverId() != null) {
            User deliveryUser = userRepository.findById(paymentDTO.getDeliverId())
                    .orElseThrow(() -> new EntityNotFoundException("Delivery user not found with id: " + paymentDTO.getDeliverId()));
            paymentData.setDeliveryUser(deliveryUser);
        }


        PaymentData savedPayment = paymentRepository.save(paymentData);
        return modelMapper.map(savedPayment, PaymentDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDTO getPaymentById(Long id) {
        PaymentData paymentData = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
        return modelMapper.map(paymentData, PaymentDTO.class);
    }
}
