package org._p1m.foodorderingsystem.features.rating.controller;

        import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
    import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
    import org._p1m.foodorderingsystem.features.rating.dto.request.RatingRequestDTO;
    import org._p1m.foodorderingsystem.features.rating.dto.response.RatingResponseDTO;
    import org._p1m.foodorderingsystem.features.rating.service.RatingService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
   import org.springframework.http.ResponseEntity;
   import org.springframework.web.bind.annotation.PostMapping;
   import org.springframework.web.bind.annotation.RequestBody;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RestController;
    @RestController
    @RequestMapping("${api.base.path}/ratings")
   public class RatingController {

               private final RatingService ratingService;

               @Autowired
       public RatingController(RatingService ratingService) {
               this.ratingService = ratingService;
            }

               @PostMapping("/delivery")
       public ResponseEntity<ApiResponse> rateDeliveryStaff(@RequestBody RatingRequestDTO requestDTO) {
                RatingResponseDTO responseDTO = ratingService.create(requestDTO);
                   ApiResponse apiResponse = ApiResponse.builder()
                                    .success(1)
                                    .code(HttpStatus.CREATED.value())
                                    .message("Rating submitted successfully.")
                                    .data(responseDTO)
                                    .build();

                            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
            }
    }