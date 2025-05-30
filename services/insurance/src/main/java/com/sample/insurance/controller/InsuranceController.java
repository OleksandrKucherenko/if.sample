package com.sample.insurance.controller;

import com.sample.insurance.model.Insurance;
import com.sample.insurance.service.InsuranceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "Insurance", description = "Insurance management endpoints")
@RestController
@RequestMapping(InsuranceController.ENTRY)
public class InsuranceController {
    
    public static final String ENTRY = "/insurances";
    
    private final InsuranceService insuranceService;
    
    @Autowired
    public InsuranceController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }
    
    @GetMapping("/ping")
    @Operation(summary = "Ping insurance service")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
    
    @GetMapping("/person/{personalIdNumber}")
    @Operation(
        summary = "Get all insurances for a person",
        description = "Retrieves all insurances for a person identified by their personal ID number",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(schema = @Schema(implementation = Insurance.class))
            ),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    public Mono<List<Insurance>> getInsurancesForPerson(
            @Parameter(description = "Personal ID number of the person", example = "920328-4428")
            @PathVariable String personalIdNumber) {
        
        return insuranceService.getInsurancesByPersonalId(personalIdNumber);
    }
}
