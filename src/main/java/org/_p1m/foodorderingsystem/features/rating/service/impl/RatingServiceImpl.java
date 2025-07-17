package org._p1m.foodorderingsystem.features.rating.service.impl;

import org._p1m.foodorderingsystem.config.exceptions.BadRequestException;
import org._p1m.foodorderingsystem.config.exceptions.EntityNotFoundException;
import org._p1m.foodorderingsystem.features.rating.dto.request.RatingRequestDTO;
import org._p1m.foodorderingsystem.features.rating.dto.response.RatingResponseDTO;
import org._p1m.foodorderingsystem.features.rating.repository.RatingRepo;
import org._p1m.foodorderingsystem.features.rating.service.RatingService;
import org._p1m.foodorderingsystem.features.users.repository.UserRepository;
import org._p1m.foodorderingsystem.model.Rating;
import org._p1m.foodorderingsystem.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
   public class RatingServiceImpl implements RatingService {

   private final RatingRepo ratingRepo;
   private final UserRepository userRepository;
   private final ModelMapper modelMapper;

   @Autowired
   public RatingServiceImpl(RatingRepo ratingRepo, UserRepository userRepository, ModelMapper modelMapper) {
        this.ratingRepo = ratingRepo;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
   }

   @Override
   public RatingResponseDTO create(RatingRequestDTO requestDTO) {
        if (requestDTO.getRatingPoints() < 1 || requestDTO.getRatingPoints() > 5) {
            throw new BadRequestException("Rating must be between 1 and 5.");
            }

                User user = userRepository.findById(requestDTO.getUserId())
                      .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + requestDTO.getUserId()));

                User deliveryUser = userRepository.findById(requestDTO.getDeliveryUserId())
                        .orElseThrow(() -> new EntityNotFoundException("Delivery user not found with id: " + requestDTO.getDeliveryUserId()));

             Rating rating = new Rating(requestDTO.getRatingPoints(), user, deliveryUser);
             Rating savedRating = ratingRepo.save(rating);

               return modelMapper.map(savedRating, RatingResponseDTO.class);
           }
}
