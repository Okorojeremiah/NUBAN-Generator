package com.nuban.nubangenerator.data.dtos.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class GenerateNUBANResponse {
    private String NUBAN;
    private String message;
    private HttpStatus status;
}
