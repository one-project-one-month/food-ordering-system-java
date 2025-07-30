package org._p1m.foodorderingsystem.features.addCart.service.impl;

import lombok.extern.slf4j.Slf4j;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
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
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddCartMenuServiceImpl implements AddCartMenuService {


    private final AddCartMenuRepo cartRepo;
    private final UserRepository userRepository;
    private final DishSizeRepo dishSizeRepository;
    private final ExtraRepo extraRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public ApiResponse addToCart(AddCartMenuRequest request) {
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
        return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
                .data(response)
                .message("Successfully added to cart.").build();
    }

    @Override
    public ApiResponse removeFromCart(Long id) {
        if (!cartRepo.existsById(id)) {
            return ApiResponse.builder()
                .success(0)
                .code(HttpStatus.NOT_FOUND.value())
                .message("Item not found in cart.")
                .data(null)
                .build();
        }

        cartRepo.deleteById(id);

        return ApiResponse.builder()
            .success(1)
            .code(HttpStatus.OK.value())
            .message("Successfully removed from cart.")
            .data(null)
            .build();
    }

    @Override
    public ApiResponse forceRemoveFromCart() {
        try {
            final String sql = "DELETE FROM add_cart_data WHERE order_id IS NULL";

            final int rowsAffected = this.jdbcTemplate.update(sql);

            log.info("Successfully force removed {} item(s) from the cart.", rowsAffected);

            return ApiResponse.builder()
                    .success(1)
                    .code(HttpStatus.OK.value())
                    .message("Successfully force removed from cart.")
                    .data(null)
                    .build();

        } catch (DataAccessException e) {
            log.error("Error during force removal from cart: {}", e.getMessage());

            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("An error occurred while cleaning the cart.")
                    .data(e.getMessage())
                    .build();
        }
    }


}