package fr.it_akademy.animal.domain;

import static fr.it_akademy.animal.domain.AnimalTestSamples.*;
import static fr.it_akademy.animal.domain.SpeciesTestSamples.*;
import static fr.it_akademy.animal.domain.TypeOfHabitatTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.it_akademy.animal.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AnimalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Animal.class);
        Animal animal1 = getAnimalSample1();
        Animal animal2 = new Animal();
        assertThat(animal1).isNotEqualTo(animal2);

        animal2.setId(animal1.getId());
        assertThat(animal1).isEqualTo(animal2);

        animal2 = getAnimalSample2();
        assertThat(animal1).isNotEqualTo(animal2);
    }

    @Test
    void speciesTest() throws Exception {
        Animal animal = getAnimalRandomSampleGenerator();
        Species speciesBack = getSpeciesRandomSampleGenerator();

        animal.setSpecies(speciesBack);
        assertThat(animal.getSpecies()).isEqualTo(speciesBack);

        animal.species(null);
        assertThat(animal.getSpecies()).isNull();
    }

    @Test
    void typeOfHabitatTest() throws Exception {
        Animal animal = getAnimalRandomSampleGenerator();
        TypeOfHabitat typeOfHabitatBack = getTypeOfHabitatRandomSampleGenerator();

        animal.addTypeOfHabitat(typeOfHabitatBack);
        assertThat(animal.getTypeOfHabitats()).containsOnly(typeOfHabitatBack);
        assertThat(typeOfHabitatBack.getAnimals()).containsOnly(animal);

        animal.removeTypeOfHabitat(typeOfHabitatBack);
        assertThat(animal.getTypeOfHabitats()).doesNotContain(typeOfHabitatBack);
        assertThat(typeOfHabitatBack.getAnimals()).doesNotContain(animal);

        animal.typeOfHabitats(new HashSet<>(Set.of(typeOfHabitatBack)));
        assertThat(animal.getTypeOfHabitats()).containsOnly(typeOfHabitatBack);
        assertThat(typeOfHabitatBack.getAnimals()).containsOnly(animal);

        animal.setTypeOfHabitats(new HashSet<>());
        assertThat(animal.getTypeOfHabitats()).doesNotContain(typeOfHabitatBack);
        assertThat(typeOfHabitatBack.getAnimals()).doesNotContain(animal);
    }
}
