package org._p1m.foodorderingsystem.features.menu.controller;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.menu.dto.request.ExtraRequest;
import org._p1m.foodorderingsystem.features.menu.dto.request.UpdateExtraRequest;
import org._p1m.foodorderingsystem.features.menu.service.ExtraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.base.path}/extras")
@Tag(name = "Extra API", description = "Endpoints for managing Extra fro menus")
public class ExtraController {

    private final ExtraService extraService;

    public ExtraController(ExtraService extraService){
        this.extraService = extraService;
    }

    @PostMapping
    @Operation(
            summary = "Create a new Extra for menu",
            description = "create a new extra  for specific menu",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request body with new dish size data",
                    required = true,
            		content = @Content(
            			    schema = @Schema(implementation = ExtraRequest.class),
            			    examples = @ExampleObject(value = """
            			    {
            			      "name": "Nga Yote Thii",
            			      "price": 100,
            			      "menuId": 10
            			    }
            			    """)
            			  )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Extra for menu created successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    public ResponseEntity<ApiResponse> createExtra(@Valid @RequestBody ExtraRequest extraRequest, HttpServletRequest request){
        final ApiResponse response = this.extraService.createExtra(extraRequest);
        return ResponseUtils.buildResponse(request, response);
    }

    @PatchMapping("/{extraId}")
    @Operation(
            summary = "Update Extra for specific menu",
            description = "Update extra for menu",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request body with update extra data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateExtraRequest.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Extra created successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    public ResponseEntity<ApiResponse> updateExtra(@Valid @RequestBody  UpdateExtraRequest updateExtraRequest,
    		@PathVariable(name="extraId") final Long extraId,
                                                      HttpServletRequest request){
        final ApiResponse response = this.extraService.updateDishSize(extraId,updateExtraRequest);
        return ResponseUtils.buildResponse(request, response);
    }

    @DeleteMapping("/{extraId}")
    @Operation(
            summary = "Delete Extra",
            description = "Deletes Extra by its ID.",
            parameters = {
                    @Parameter(name = "extraId", description = "Extra Id  to delete", required = true)
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Extra delete successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Extra not found")
            }
    )
    public ResponseEntity<ApiResponse> deleteExtra(@PathVariable(name="extraId") Long extraId, HttpServletRequest request) {
        final ApiResponse response = this.extraService.deleteMenu(extraId);
        return ResponseUtils.buildResponse(request, response);
    }
}
