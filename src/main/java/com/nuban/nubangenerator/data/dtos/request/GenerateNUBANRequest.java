package com.nuban.nubangenerator.data.dtos.request;

import lombok.Data;

@Data
public class GenerateNUBANRequest {
    private String bankCode;
    private String serialNumber;
}
