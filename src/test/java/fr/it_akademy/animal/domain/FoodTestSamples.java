package fr.it_akademy.animal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FoodTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Food getFoodSample1() {
        return new Food().id(1L).name("name1").color("color1");
    }

    public static Food getFoodSample2() {
        return new Food().id(2L).name("name2").color("color2");
    }

    public static Food getFoodRandomSampleGenerator() {
        return new Food().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).color(UUID.randomUUID().toString());
    }
}
