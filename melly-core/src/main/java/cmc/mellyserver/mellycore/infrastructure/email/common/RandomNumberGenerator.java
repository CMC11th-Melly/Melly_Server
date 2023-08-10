package cmc.mellyserver.mellycore.infrastructure.email.common;

import java.util.Random;

public class RandomNumberGenerator {

    public static final String makeRandomNumber() {
        Random random = new Random();
        return String.valueOf(random.nextInt(900000));
    }
}
