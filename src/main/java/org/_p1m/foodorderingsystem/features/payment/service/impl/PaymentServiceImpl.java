package org._p1m.foodorderingsystem.features.payment.service.impl;

import java.time.LocalDateTime;

import org._p1m.foodorderingsystem.common.storage.StorageService;
import org._p1m.foodorderingsystem.common.storage.StorageServiceFactory;
import org._p1m.foodorderingsystem.config.exceptions.EntityNotFoundException;
import org._p1m.foodorderingsystem.features.order.repository.OrderRepo;
import org._p1m.foodorderingsystem.features.payment.dto.PaymentRequestDTO;
import org._p1m.foodorderingsystem.features.payment.dto.PaymentResponseDTO;
import org._p1m.foodorderingsystem.features.payment.repository.PaymentRepository;
import org._p1m.foodorderingsystem.features.payment.service.PaymentService;
import org._p1m.foodorderingsystem.features.restaurant.repository.RestaurantRepository;
import org._p1m.foodorderingsystem.features.users.repository.UserRepository;
import org._p1m.foodorderingsystem.model.OrderData;
import org._p1m.foodorderingsystem.model.PaymentData;
import org._p1m.foodorderingsystem.model.Profile;
import org._p1m.foodorderingsystem.model.Restaurant;
import org._p1m.foodorderingsystem.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepo orderRepository;
    private final ModelMapper modelMapper;
    private StorageService storageService;
    
    @Autowired
    public void setStorageService(StorageServiceFactory factory) {
        this.storageService = factory.getConfiguredStorageService();
    }

    @Override
    @Transactional
    public PaymentResponseDTO createPayment(PaymentRequestDTO paymentDTO) {
        PaymentData paymentData = new PaymentData();

        OrderData order = orderRepository.findById(paymentDTO.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + paymentDTO.getOrderId()));
        paymentData.setOrder(order);

        User user = userRepository.findById(paymentDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + paymentDTO.getUserId()));
        paymentData.setUser(user);

        Restaurant restaurant = restaurantRepository.findById(paymentDTO.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + paymentDTO.getRestaurantId()));
        paymentData.setRestaurant(restaurant);
        paymentData.setDateTime(LocalDateTime.now());
        // Set fields from DTO
//        paymentData.setPaymentScreenshot(paymentDTO.getPaymentScreenshot());
        // paymentData.setPaymentMethod(paymentDTO.getPaymentMethod()); // Assuming PaymentData has this field

        PaymentData savedPayment = paymentRepository.save(paymentData);
        return modelMapper.map(savedPayment, PaymentResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentById(Long id) {
        PaymentData paymentData = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
        return modelMapper.map(paymentData, PaymentResponseDTO.class);
    }

	@Override
	@Transactional
	public String uploadPaymentPicture(Long paymentId, MultipartFile file) {
		final PaymentData payment = this.paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found for ID: " + paymentId));

        final String filename = storageService.store(file);

        final String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/")
                .path(filename)
                .toUriString();

        payment.setPaymentScreenshot(fileUrl);
        this.paymentRepository.save(payment);
        return fileUrl;
	}
}