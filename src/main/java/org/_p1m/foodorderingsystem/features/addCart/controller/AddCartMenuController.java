//package org._p1m.foodorderingsystem.features.addCart.controller;
//
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class AddCartMenuController {
//}

package org._p1m.foodorderingsystem.features.addCart.controller;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.addCart.dto.request.AddCartMenuRequest;
import org._p1m.foodorderingsystem.features.addCart.service.AddCartMenuService;
import org._p1m.foodorderingsystem.features.address.dto.request.AddressCreateRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.base.path}/cart")
@RequiredArgsConstructor
public class AddCartMenuController {

    private final AddCartMenuService addCartMenuService;

    @PostMapping("/add")
    @Operation(
            summary = "Add item to cart.",
            description = "Add an item to a cart with request body.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Added cart request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AddCartMenuRequest.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully added to cart."),
            }
    )
    public ResponseEntity<ApiResponse> addToCart(@RequestBody AddCartMenuRequest cartRequest, HttpServletRequest request) {
    	ApiResponse response = addCartMenuService.addToCart(cartRequest);
        return ResponseUtils.buildResponse(request, response);
    }
    
    @DeleteMapping("/remove/{id}")
    @Operation(
            summary = "Remove cart item.",
            description = "Remove an item from cart with cart id.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully removed from cart."),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Item not found in cart.")
            }
    )
    public ResponseEntity<ApiResponse> removeFromCart(
    		@Parameter(name = "id", description = "Cart ID", required = true)
    		@PathVariable(name="id") Long id, HttpServletRequest request) {
    	ApiResponse response = addCartMenuService.removeFromCart(id);
        return ResponseUtils.buildResponse(request, response);
    }

    @DeleteMapping("/remove/force")
    @Operation(
            summary = "Remove cart item.",
            description = "Remove an item from cart ",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully force removed from cart."),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "An error occurred while cleaning the cart.")
            }
    )
    public ResponseEntity<ApiResponse> forceRemoveFromCart(
            HttpServletRequest request) {
        final ApiResponse response = addCartMenuService.forceRemoveFromCart();
        return ResponseUtils.buildResponse(request, response);
    }

    @GetMapping("/customer/{customerId}")
    @Operation(
            summary = "Get all items in cart for a customer.",
            description = "Retrieves a list of all items in a customer's cart that have not been ordered yet.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved cart items."),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found."),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Item not found in cart.")
            }
    )
    public ResponseEntity<ApiResponse> getCartItemsByCustomerId(
            @Parameter(name = "customerId", description = "The ID of the customer", required = true)
            @PathVariable(name = "customerId") final Long customerId,
            final HttpServletRequest request) {
        ApiResponse response = addCartMenuService.getCartItemsByCustomerId(customerId);
        return ResponseUtils.buildResponse(request, response);
    }
    
    @GetMapping("/restaurantOwner/{orderId}")
    @Operation(
            summary = "Get all items in cart for restaurant owner to check order.",
            description = "Retrieves a list of all items in a customer's cart that have been ordered to restaurant.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved cart items."),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found."),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Item not found in cart.")
            }
    )
    public ResponseEntity<ApiResponse> getCartItemsByOrderId(
            @Parameter(name = "orderId", description = "The ID of the order", required = true)
            @PathVariable(name = "orderId") final Long orderId,
            final HttpServletRequest request) {
        ApiResponse response = addCartMenuService.getCartItemsByOrderId(orderId);
        return ResponseUtils.buildResponse(request, response);
    }
}
