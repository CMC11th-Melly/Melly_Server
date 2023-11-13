package cmc.mellyserver.auth.certification;

import java.util.Random;

public class RandomNumberGenerator {

  public static final String makeRandomNumber() {
	Random random = new Random();
	return String.valueOf(random.nextInt(900000));
  }

}
