package org._p1m.foodorderingsystem.features.rating.service;

import org._p1m.foodorderingsystem.features.rating.dto.request.RatingRequestDTO;
import org._p1m.foodorderingsystem.features.rating.dto.response.RatingResponseDTO;

public interface RatingService {
   RatingResponseDTO create(RatingRequestDTO rateDeliveryRequestDTO);
   }
