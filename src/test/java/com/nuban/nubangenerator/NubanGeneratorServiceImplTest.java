package com.nuban.nubangenerator;

import com.nuban.nubangenerator.data.dtos.request.GenerateNUBANRequest;
import com.nuban.nubangenerator.data.dtos.response.GenerateNUBANResponse;
import com.nuban.nubangenerator.exception.BankNotFoundException;
import com.nuban.nubangenerator.repository.NubanRepo;
import com.nuban.nubangenerator.service.NubanGeneratorService;
import com.nuban.nubangenerator.service.implementation.NubanGeneratorServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

public class NubanGeneratorServiceImplTest {
    private NubanGeneratorService nubanGeneratorService;

    @Mock
    private NubanRepo nubanRepo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        nubanGeneratorService = new NubanGeneratorServiceImpl(nubanRepo);
    }

    @Test
    public void testGenerateNUBAN_Successful() {
        // Mock the repository behavior
        Mockito.when(nubanRepo.save(Mockito.any())).thenReturn(null);

        // Create a test request
        GenerateNUBANRequest request = new GenerateNUBANRequest();
        request.setBankCode("011");
        request.setSerialNumber("000001457");

        // Call the method under test
        GenerateNUBANResponse response = nubanGeneratorService.generateNUBAN(request);

        // Verify the response
        Assertions.assertNotNull(response);
        Assertions.assertEquals("0000014579", response.getNUBAN());
        Assertions.assertEquals("NUBAN generated successfully", response.getMessage());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatus());

        // Verify the repository interaction
        Mockito.verify(nubanRepo, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void testGenerateNUBAN_InvalidSerialNumberLength() {
        // Create a test request with an invalid serial number
        GenerateNUBANRequest request = new GenerateNUBANRequest();
        request.setBankCode("058");
        request.setSerialNumber("1234567890"); // Serial number is longer than expected

        // Call the method under test and expect an exception
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            nubanGeneratorService.generateNUBAN(request);
        });

        // Verify that the repository was not called
        Mockito.verifyNoInteractions(nubanRepo);
    }

    @Test
    public void testGenerateNUBAN_SerialNumberCannotContainLetters() {
        // Create a test request with an invalid serial number
        GenerateNUBANRequest request = new GenerateNUBANRequest();
        request.setBankCode("058");
        request.setSerialNumber("123456ABC"); // Serial number with letters

        // Call the method under test and expect an exception
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            nubanGeneratorService.generateNUBAN(request);
        });

        // Verify that the repository was not called
        Mockito.verifyNoInteractions(nubanRepo);
    }

    @Test
    public void testGenerateNUBAN_SerialNumberCannotContainNegativeDigit() {
        // Create a test request with an invalid serial number
        GenerateNUBANRequest request = new GenerateNUBANRequest();
        request.setBankCode("058");
        request.setSerialNumber("-1234564"); // Serial number with negative digit

        // Call the method under test and expect an exception
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            nubanGeneratorService.generateNUBAN(request);
        });

        // Verify that the repository was not called
        Mockito.verifyNoInteractions(nubanRepo);
    }

    @Test
    public void testGenerateNUBAN_InvalidBankCodeLength() {
        // Create a test request with an invalid serial number
        GenerateNUBANRequest request = new GenerateNUBANRequest();
        request.setBankCode("058333");
        request.setSerialNumber("12345678"); // Bank code is longer than expected

        // Call the method under test and expect an exception
        Assertions.assertThrows(BankNotFoundException.class, () -> {
            nubanGeneratorService.generateNUBAN(request);
        });

        // Verify that the repository was not called
        Mockito.verifyNoInteractions(nubanRepo);
    }

    @Test
    public void testGenerateNUBAN_BankCodeCannotContainLetters() {
        // Create a test request with an invalid bank code
        GenerateNUBANRequest request = new GenerateNUBANRequest();
        request.setBankCode("XYZ"); // Bank code with letters
        request.setSerialNumber("123456789");

        // Call the method under test and expect an exception
        Assertions.assertThrows(BankNotFoundException.class, () -> {
            nubanGeneratorService.generateNUBAN(request);
        });

        // Verify that the repository was not called
        Mockito.verifyNoInteractions(nubanRepo);
    }

    @Test
    public void testGenerateNUBAN_BankCodeCannotContainNegativeDigit() {
        // Create a test request with an invalid bank code
        GenerateNUBANRequest request = new GenerateNUBANRequest();
        request.setBankCode("-123"); // Bank code with negative digit
        request.setSerialNumber("123456789");

        // Call the method under test and expect an exception
        Assertions.assertThrows(BankNotFoundException.class, () -> {
            nubanGeneratorService.generateNUBAN(request);
        });

        // Verify that the repository was not called
        Mockito.verifyNoInteractions(nubanRepo);
    }

}
