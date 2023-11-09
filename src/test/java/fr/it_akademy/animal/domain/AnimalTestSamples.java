package fr.it_akademy.animal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AnimalTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Animal getAnimalSample1() {
        return new Animal().id(1L).name("name1").age(1L).character("character1");
    }

    public static Animal getAnimalSample2() {
        return new Animal().id(2L).name("name2").age(2L).character("character2");
    }

    public static Animal getAnimalRandomSampleGenerator() {
        return new Animal()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .age(longCount.incrementAndGet())
            .character(UUID.randomUUID().toString());
    }
}
