//package org._p1m.foodorderingsystem.features.order.service.impl;
//
//import org._p1m.foodorderingsystem.config.exceptions.EntityNotFoundException;
//import org._p1m.foodorderingsystem.features.order.dto.request.PaymentRequestDTO;
//import org._p1m.foodorderingsystem.features.order.dto.response.PaymentResponseDTO;
//import org._p1m.foodorderingsystem.features.order.repository.PaymentRepo;
//import org._p1m.foodorderingsystem.features.order.service.PaymentService;
//import org._p1m.foodorderingsystem.features.restaurant.repository.GetRestaurantListRepo;
//import org._p1m.foodorderingsystem.features.users.repository.UserRepository;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org._p1m.foodorderingsystem.model.*;
//
//@Service
//public class PaymentServiceImpl implements PaymentService {
//
//    private final PaymentRepo paymentRepo;
//    private final UserRepository userRepository;
//    private final GetRestaurantListRepo getRestaurantListRepo;
////    private final OrderDataRepo orderDataRepo;
//    // Assuming CouponRepo exists
//    // private final CouponRepo couponRepo;
//    private final ModelMapper modelMapper;
//
//    @Autowired
//    public PaymentServiceImpl(PaymentRepo paymentRepo, UserRepository userRepository, GetRestaurantListRepo getRestaurantListRepo,  ModelMapper
//            modelMapper) {
//        this.paymentRepo = paymentRepo;
//        this.userRepository = userRepository;
//        this.getRestaurantListRepo = getRestaurantListRepo;
//        this.modelMapper = modelMapper;
//    }
//
//    @Override
//    public PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO) {
//        User user = userRepository.findById(paymentRequestDTO.getUserId())
//                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + paymentRequestDTO.getUserId()));
//    }
//}