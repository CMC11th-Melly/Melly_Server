package cmc.mellyserver.auth.certification;

import java.util.Random;

public abstract class RandomNumberGenerator {

    private static final int RANDOM_BOUND = 900_000;

    public static String makeRandomNumber() {
        Random random = new Random();
        return String.valueOf(random.nextInt(RANDOM_BOUND));
    }

}
