package fr.it_akademy.animal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SpeciesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Species getSpeciesSample1() {
        return new Species().id(1L).name("name1").size(1L);
    }

    public static Species getSpeciesSample2() {
        return new Species().id(2L).name("name2").size(2L);
    }

    public static Species getSpeciesRandomSampleGenerator() {
        return new Species().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).size(longCount.incrementAndGet());
    }
}
