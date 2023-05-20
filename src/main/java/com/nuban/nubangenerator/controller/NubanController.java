package com.nuban.nubangenerator.controller;

import com.nuban.nubangenerator.data.dtos.request.GenerateNUBANRequest;
import com.nuban.nubangenerator.data.dtos.response.ApiResponse;
import com.nuban.nubangenerator.exception.BankNotFoundException;
import com.nuban.nubangenerator.service.NubanGeneratorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("api/v1/user/")
public class NubanController {
    private final NubanGeneratorService nubanGeneratorService;

    public NubanController(NubanGeneratorService nubanGeneratorService) {
        this.nubanGeneratorService = nubanGeneratorService;
    }


    @PostMapping("generateNUBAN")
    public ResponseEntity<ApiResponse> generateNUBAN(
            @RequestBody GenerateNUBANRequest request, HttpServletRequest httpServletRequest) {

        try {
            return ResponseEntity.ok(ApiResponse.builder()
                    .path(httpServletRequest.getRequestURI())
                    .data(nubanGeneratorService.generateNUBAN(request))
                    .timeStamp(ZonedDateTime.now())
                    .isSuccessful(true)
                    .statusCode(HttpStatus.OK.value())
                    .build());
        }catch (BankNotFoundException | IllegalArgumentException e){
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(e.getMessage())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build());
        }
    }


}
