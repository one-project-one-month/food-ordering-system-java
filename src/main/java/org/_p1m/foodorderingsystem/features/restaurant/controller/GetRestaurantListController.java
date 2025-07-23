package org._p1m.foodorderingsystem.features.restaurant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurants")
public class GetRestaurantListController {

    @GetMapping("/getRes")
    public String get(){
        return "hello";
    }

}
