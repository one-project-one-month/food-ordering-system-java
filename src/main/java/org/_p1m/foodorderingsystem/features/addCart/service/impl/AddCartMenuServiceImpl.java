package org._p1m.foodorderingsystem.features.addCart.service.impl;

import org._p1m.foodorderingsystem.features.addCart.dto.request.AddCartMenuRequest;
import org._p1m.foodorderingsystem.features.addCart.dto.response.AddCartMenuResponse;
import org._p1m.foodorderingsystem.features.addCart.repository.AddCartMenuRepo;
import org._p1m.foodorderingsystem.features.addCart.service.AddCartMenuService;
import org._p1m.foodorderingsystem.features.menu.repository.DishSizeRepo;
import org._p1m.foodorderingsystem.features.menu.repository.ExtraRepo;
import org._p1m.foodorderingsystem.features.users.repository.UserRepository;
import org._p1m.foodorderingsystem.model.AddCartData;
import org._p1m.foodorderingsystem.model.DishSize;
import org._p1m.foodorderingsystem.model.Extra;
import org._p1m.foodorderingsystem.model.User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddCartMenuServiceImpl implements AddCartMenuService {


    private final AddCartMenuRepo cartRepo;
    private final UserRepository userRepository;
    private final DishSizeRepo dishSizeRepository;
    private final ExtraRepo extraRepository;

    @Override
    public AddCartMenuResponse addToCart(AddCartMenuRequest request) {
        User user = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        DishSize dishSize = null;
        if (request.getDishSizeId() != null) {
            dishSize = dishSizeRepository.findById(request.getDishSizeId())
                    .orElseThrow(() -> new RuntimeException("DishSize not found"));
        }

        Extra extra = null;
        if (request.getExtraId() != null) {
            extra = extraRepository.findById(request.getExtraId())
                    .orElseThrow(() -> new RuntimeException("Extra not found"));
        }

        AddCartData cart = new AddCartData(
                request.getQuantity(),
                user,
                dishSize,
                extra,
                null
        );

        AddCartData saved = cartRepo.save(cart);

        AddCartMenuResponse response = new AddCartMenuResponse();
        response.setCartId(saved.getId());
        response.setQuantity(saved.getQuantity());
        response.setCustomerId(saved.getCustomer().getId());
        response.setDishSizeId(saved.getDishSize() != null ? saved.getDishSize().getId() : null);
        response.setExtraId(saved.getExtra() != null ? saved.getExtra().getId() : null);
        return response;
    }


}