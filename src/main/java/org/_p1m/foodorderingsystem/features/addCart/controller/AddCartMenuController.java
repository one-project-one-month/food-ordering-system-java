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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
