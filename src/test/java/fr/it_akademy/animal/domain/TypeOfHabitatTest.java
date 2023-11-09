package fr.it_akademy.animal.domain;

import static fr.it_akademy.animal.domain.AnimalTestSamples.*;
import static fr.it_akademy.animal.domain.TypeOfHabitatTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.it_akademy.animal.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TypeOfHabitatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeOfHabitat.class);
        TypeOfHabitat typeOfHabitat1 = getTypeOfHabitatSample1();
        TypeOfHabitat typeOfHabitat2 = new TypeOfHabitat();
        assertThat(typeOfHabitat1).isNotEqualTo(typeOfHabitat2);

        typeOfHabitat2.setId(typeOfHabitat1.getId());
        assertThat(typeOfHabitat1).isEqualTo(typeOfHabitat2);

        typeOfHabitat2 = getTypeOfHabitatSample2();
        assertThat(typeOfHabitat1).isNotEqualTo(typeOfHabitat2);
    }

    @Test
    void animalTest() throws Exception {
        TypeOfHabitat typeOfHabitat = getTypeOfHabitatRandomSampleGenerator();
        Animal animalBack = getAnimalRandomSampleGenerator();

        typeOfHabitat.addAnimal(animalBack);
        assertThat(typeOfHabitat.getAnimals()).containsOnly(animalBack);

        typeOfHabitat.removeAnimal(animalBack);
        assertThat(typeOfHabitat.getAnimals()).doesNotContain(animalBack);

        typeOfHabitat.animals(new HashSet<>(Set.of(animalBack)));
        assertThat(typeOfHabitat.getAnimals()).containsOnly(animalBack);

        typeOfHabitat.setAnimals(new HashSet<>());
        assertThat(typeOfHabitat.getAnimals()).doesNotContain(animalBack);
    }
}
