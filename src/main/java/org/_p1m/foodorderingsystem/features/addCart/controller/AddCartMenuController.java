//package org._p1m.foodorderingsystem.features.addCart.controller;
//
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class AddCartMenuController {
//}

package org._p1m.foodorderingsystem.features.addCart.controller;

import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.features.addCart.dto.request.AddCartMenuRequest;
import org._p1m.foodorderingsystem.features.addCart.dto.response.AddCartMenuResponse;
import org._p1m.foodorderingsystem.features.addCart.service.AddCartMenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base.path}/cart")
@RequiredArgsConstructor
public class AddCartMenuController {

    private final AddCartMenuService addCartMenuService;

    @PostMapping("/add")
    public ResponseEntity<AddCartMenuResponse> addToCart(@RequestBody AddCartMenuRequest request) {
        AddCartMenuResponse response = addCartMenuService.addToCart(request);
        return ResponseEntity.ok(response);
    }
}
