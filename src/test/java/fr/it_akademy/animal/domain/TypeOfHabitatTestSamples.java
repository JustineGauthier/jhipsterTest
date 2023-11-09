package fr.it_akademy.animal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TypeOfHabitatTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TypeOfHabitat getTypeOfHabitatSample1() {
        return new TypeOfHabitat().id(1L).categorie("categorie1").location("location1").ground("ground1");
    }

    public static TypeOfHabitat getTypeOfHabitatSample2() {
        return new TypeOfHabitat().id(2L).categorie("categorie2").location("location2").ground("ground2");
    }

    public static TypeOfHabitat getTypeOfHabitatRandomSampleGenerator() {
        return new TypeOfHabitat()
            .id(longCount.incrementAndGet())
            .categorie(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .ground(UUID.randomUUID().toString());
    }
}
