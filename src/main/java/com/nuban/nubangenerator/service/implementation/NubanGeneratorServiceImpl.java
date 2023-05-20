package com.nuban.nubangenerator.service.implementation;
import com.nuban.nubangenerator.data.dtos.request.GenerateNUBANRequest;
import com.nuban.nubangenerator.data.dtos.response.GenerateNUBANResponse;
import com.nuban.nubangenerator.data.model.Nuban;

import static com.nuban.nubangenerator.utils.AppUtils.*;

import com.nuban.nubangenerator.exception.BankNotFoundException;
import com.nuban.nubangenerator.repository.NubanRepo;
import com.nuban.nubangenerator.service.NubanGeneratorService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


/**
 * This class provides functionality for generating NUBAN (Nigerian Uniform Bank Account Number) codes.
 * The algorithm source can be found at: <a href="https://www.cbn.gov.ng/OUT/2011/CIRCULARS/BSPD/NUBAN%20PROPOSALS%20V%200%204-%2003%2009%202010.PDF">...</a>"
 * The approved NUBAN format is ABC-DEFGHIJKL-M where:
 *   - ABC is the 3-digit bank code assigned by the CBN
 *   - DEFGHIJKL is the NUBAN account serial number
 *   - M is the NUBAN check digit required for account number validation
 * <p>
 * params: request body; contains the serial number and the bank code
 */


@Service
public class NubanGeneratorServiceImpl implements NubanGeneratorService {
    private final NubanRepo nubanRepository;

    public NubanGeneratorServiceImpl(NubanRepo nubanRepository) {
        this.nubanRepository = nubanRepository;
    }

    @Override
    public GenerateNUBANResponse generateNUBAN(GenerateNUBANRequest request) {

        serialNumberAndBankCodeCheck(request.getBankCode(), request.getSerialNumber());

        String serialNumber = String.format("%0" + serialNumLength + "d", Integer.parseInt(request.getSerialNumber()));
        String base = request.getBankCode() + serialNumber;

        int checkDigit = getCheckDigit(base);
        String nuban = serialNumber + checkDigit;

        Nuban nubanToSave = new Nuban();
        nubanToSave.setNUBAN(nuban);
        nubanRepository.save(nubanToSave);

        return getGenerateNUBANResponse(nuban);
    }

    private static void serialNumberAndBankCodeCheck(String bankCode, String serialNumber) {
        if (serialNumber.length() > serialNumLength || Integer.parseInt(serialNumber ) < 0) {
            throw new IllegalArgumentException("Serial number should be at most " + serialNumLength + "-digits long.");
        }

        if (bankCode.length() > bankCodeLength || bankCode.length() < bankCodeLength) {
            throw new BankNotFoundException("The inputted code is not a Nigerian commercial bank code, please input a correct code");
        }

        for (int i = 0; i < serialNumber.length(); i++) {
            if (Character.isLetter(serialNumber.charAt(i))){
                throw new IllegalArgumentException("Serial number cannot contain alphabet");
            }
        }

        for (int i = 0; i < bankCode.length(); i++) {
            if (Character.isLetter(bankCode.charAt(i))){
                throw new BankNotFoundException("Bank code cannot contain alphabet");
            }
        }

    }

    private static GenerateNUBANResponse getGenerateNUBANResponse(String nuban) {
        return GenerateNUBANResponse.builder()
                .NUBAN(nuban)
                .message("NUBAN generated successfully")
                .status(HttpStatus.CREATED)
                .build();
    }

    private static int getCheckDigit(String base) {
        int sum = 0;

        for (int i = 0; i < base.length(); i++) {
            sum += Character.getNumericValue(base.charAt(i)) * Character.getNumericValue(seed.charAt(i));
        }

        sum %= 10;
        int checkDigit = 10 - sum;
        checkDigit = (checkDigit == 10) ? 0 : checkDigit;
        return checkDigit;
    }
    
}


