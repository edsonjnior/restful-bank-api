package com.github.edsonjnior.bankapi.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.IntStream;

@Component
public class Utils {

    private static final Random RANDOM = new SecureRandom();
    private static final String DIGITS = "0123456789X";

    public String generateNumberAccount(){
        int length = RANDOM.ints(4, 6).limit(1).findFirst().getAsInt();

        StringBuilder returnValue = new StringBuilder(length);

        IntStream.range(0, length).forEach(n -> {
            var generatedNumber = RANDOM.nextInt(10);
            while (n == 0 && generatedNumber == 0){
                generatedNumber = RANDOM.nextInt(10);
            }
            returnValue.append(generatedNumber);
        });

        return returnValue
                .append("-")
                .append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())))
                .toString();
    }


}
