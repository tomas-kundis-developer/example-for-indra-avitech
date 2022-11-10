package test.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class TestUtils {

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public static int randInt(int min, int max) {
    assertThat(min)
        .isNotNegative()
        .isLessThan(max);

    assertThat(max).isGreaterThan(min);

    return new Random().nextInt(min + max) + min;
  }
}
