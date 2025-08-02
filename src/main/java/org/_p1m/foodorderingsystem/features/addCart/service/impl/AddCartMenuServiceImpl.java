package org._p1m.foodorderingsystem.features.addCart.service.impl;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.addCart.dto.request.AddCartMenuRequest;
import org._p1m.foodorderingsystem.features.addCart.dto.response.AddCartMenuResponse;
import org._p1m.foodorderingsystem.features.addCart.dto.response.CartItemDetailResponse;
import org._p1m.foodorderingsystem.features.addCart.dto.response.DishResponse;
import org._p1m.foodorderingsystem.features.addCart.dto.response.ExtraResponse;
import org._p1m.foodorderingsystem.features.addCart.dto.response.MenuResponse;
import org._p1m.foodorderingsystem.features.addCart.dto.response.RestaurantResponse;
import org._p1m.foodorderingsystem.features.addCart.repository.AddCartMenuRepo;
import org._p1m.foodorderingsystem.features.addCart.service.AddCartMenuService;
import org._p1m.foodorderingsystem.features.menu.repository.DishSizeRepo;
import org._p1m.foodorderingsystem.features.menu.repository.ExtraRepo;
import org._p1m.foodorderingsystem.features.menu.repository.ManageMenuRepository;
import org._p1m.foodorderingsystem.features.processOrder.repository.OrderRepository;
import org._p1m.foodorderingsystem.features.users.repository.UserRepository;
import org._p1m.foodorderingsystem.model.AddCartData;
import org._p1m.foodorderingsystem.model.DishSize;
import org._p1m.foodorderingsystem.model.Extra;
import org._p1m.foodorderingsystem.model.Menu;
import org._p1m.foodorderingsystem.model.Restaurant;
import org._p1m.foodorderingsystem.model.User;
import org._p1m.foodorderingsystem.model.UserDetail;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddCartMenuServiceImpl implements AddCartMenuService {


    private final AddCartMenuRepo cartRepo;
    private final UserRepository userRepository;
    private final DishSizeRepo dishSizeRepository;
    private final ExtraRepo extraRepository;
    private final JdbcTemplate jdbcTemplate;
    private final ManageMenuRepository menuRepository;
    private final OrderRepository orderRepository;
    
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
        	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        	UserDetail userDetails = (UserDetail) authentication.getPrincipal();

        	String email = userDetails.getUsername();
        	
//            final String sql = "DELETE FROM add_cart_data WHERE order_id IS NULL";
        	final String sql = "DELETE acd from add_cart_data acd JOIN users u ON acd.customer_id = u.id WHERE acd.order_id IS NULL AND u.email = ?";

            final int rowsAffected = this.jdbcTemplate.update(sql,email);

            log.info("Successfully force removed {} item(s) from the cart for email={}.", rowsAffected,email);

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

    @Override
    public ApiResponse getCartItemsByCustomerId(final Long customerId) {
        return getCartItems(
            userRepository.existsById(customerId),
            () -> cartRepo.findUnorderedCartItemsByCustomerId(customerId),
            "Customer not found.",
            "Successfully retrieved cart items."
        );
    }

    @Override
    public ApiResponse getCartItemsByOrderId(final Long orderId) {
        return getCartItems(
            orderRepository.existsById(orderId),
            () -> cartRepo.findCartItemsByOrderId(orderId),
            "Order not found.",
            "Successfully retrieved cart items."
        );
    }
    
    private ApiResponse getCartItems(
            boolean exists,
            Supplier<List<AddCartData>> cartSupplier,
            String notFoundMessage,
            String successMessage
    ) {
        if (!exists) {
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(notFoundMessage)
                    .build();
        }

        List<AddCartData> cartItems = cartSupplier.get();

        if (CollectionUtils.isEmpty(cartItems)) {
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("Item not found in cart.")
                    .data(null)
                    .build();
        }

        List<CartItemDetailResponse> responseData = cartItems.stream()
                .map(this::mapToCartItemDetail)
                .collect(Collectors.toList());

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message(successMessage)
                .data(responseData)
                .build();
    }
    
    private CartItemDetailResponse mapToCartItemDetail(final AddCartData cartItem) {
        final DishSize dishSize = cartItem.getDishSize();
        final Extra extra = cartItem.getExtra();
        final Menu menu = dishSize.getMenu();
        
        final DishResponse dishResponse = getDishResponse(dishSize);
        
        ExtraResponse extraResponse = null;
        if (extra != null) {
            extraResponse = new ExtraResponse(
                    extra.getId(),
                    extra.getName(),
                    extra.getPrice()
            );
        }
        MenuResponse menuResponse = null;
        
        if(menu != null) {
        	menuResponse = new MenuResponse(
        			menu.getId(),
                    menu.getDish(),
                    menu.getDishImg(),
                    menu.getPrice(),
                    menu.getStatus()
            );
        }
        return CartItemDetailResponse.builder()
                .cartId(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .dish(dishResponse)
                .extra(extraResponse)
                .menu(menuResponse)
                .build();
    }

    private static DishResponse getDishResponse(final DishSize dishSize) {
        DishResponse dishResponse = null;
        if (dishSize != null && dishSize.getMenu() != null) {
            final Menu menu = dishSize.getMenu();
            final Restaurant restaurant = menu.getRestaurant();

            RestaurantResponse restaurantResponse = null;
            if (restaurant != null) {
                restaurantResponse = new RestaurantResponse(
                        restaurant.getId(),
                        restaurant.getRestaurantName()
                );
            }

            dishResponse = new DishResponse(
                    menu.getId(),
                    menu.getDish(),
                    menu.getDishImg(),
                    dishSize.getPrice(),
                    restaurantResponse
            );
        }
        return dishResponse;
    }
}