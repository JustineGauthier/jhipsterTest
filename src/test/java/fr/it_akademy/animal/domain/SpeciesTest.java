package fr.it_akademy.animal.domain;

import static fr.it_akademy.animal.domain.AnimalTestSamples.*;
import static fr.it_akademy.animal.domain.SpeciesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.it_akademy.animal.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SpeciesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Species.class);
        Species species1 = getSpeciesSample1();
        Species species2 = new Species();
        assertThat(species1).isNotEqualTo(species2);

        species2.setId(species1.getId());
        assertThat(species1).isEqualTo(species2);

        species2 = getSpeciesSample2();
        assertThat(species1).isNotEqualTo(species2);
    }

    @Test
    void animalTest() throws Exception {
        Species species = getSpeciesRandomSampleGenerator();
        Animal animalBack = getAnimalRandomSampleGenerator();

        species.addAnimal(animalBack);
        assertThat(species.getAnimals()).containsOnly(animalBack);
        assertThat(animalBack.getSpecies()).isEqualTo(species);

        species.removeAnimal(animalBack);
        assertThat(species.getAnimals()).doesNotContain(animalBack);
        assertThat(animalBack.getSpecies()).isNull();

        species.animals(new HashSet<>(Set.of(animalBack)));
        assertThat(species.getAnimals()).containsOnly(animalBack);
        assertThat(animalBack.getSpecies()).isEqualTo(species);

        species.setAnimals(new HashSet<>());
        assertThat(species.getAnimals()).doesNotContain(animalBack);
        assertThat(animalBack.getSpecies()).isNull();
    }
}
