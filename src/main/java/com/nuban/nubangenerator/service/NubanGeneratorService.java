package com.nuban.nubangenerator.service;

import com.nuban.nubangenerator.data.dtos.request.GenerateNUBANRequest;
import com.nuban.nubangenerator.data.dtos.response.GenerateNUBANResponse;

public interface NubanGeneratorService {
    GenerateNUBANResponse generateNUBAN(GenerateNUBANRequest request);
}
