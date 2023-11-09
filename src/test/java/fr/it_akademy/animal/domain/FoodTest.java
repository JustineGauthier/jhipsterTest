package fr.it_akademy.animal.domain;

import static fr.it_akademy.animal.domain.AnimalTestSamples.*;
import static fr.it_akademy.animal.domain.FoodTestSamples.*;
import static fr.it_akademy.animal.domain.TypeOfHabitatTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.it_akademy.animal.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FoodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Food.class);
        Food food1 = getFoodSample1();
        Food food2 = new Food();
        assertThat(food1).isNotEqualTo(food2);

        food2.setId(food1.getId());
        assertThat(food1).isEqualTo(food2);

        food2 = getFoodSample2();
        assertThat(food1).isNotEqualTo(food2);
    }

    @Test
    void animalTest() throws Exception {
        Food food = getFoodRandomSampleGenerator();
        Animal animalBack = getAnimalRandomSampleGenerator();

        food.addAnimal(animalBack);
        assertThat(food.getAnimals()).containsOnly(animalBack);

        food.removeAnimal(animalBack);
        assertThat(food.getAnimals()).doesNotContain(animalBack);

        food.animals(new HashSet<>(Set.of(animalBack)));
        assertThat(food.getAnimals()).containsOnly(animalBack);

        food.setAnimals(new HashSet<>());
        assertThat(food.getAnimals()).doesNotContain(animalBack);
    }

    @Test
    void typeOfHabitatTest() throws Exception {
        Food food = getFoodRandomSampleGenerator();
        TypeOfHabitat typeOfHabitatBack = getTypeOfHabitatRandomSampleGenerator();

        food.addTypeOfHabitat(typeOfHabitatBack);
        assertThat(food.getTypeOfHabitats()).containsOnly(typeOfHabitatBack);

        food.removeTypeOfHabitat(typeOfHabitatBack);
        assertThat(food.getTypeOfHabitats()).doesNotContain(typeOfHabitatBack);

        food.typeOfHabitats(new HashSet<>(Set.of(typeOfHabitatBack)));
        assertThat(food.getTypeOfHabitats()).containsOnly(typeOfHabitatBack);

        food.setTypeOfHabitats(new HashSet<>());
        assertThat(food.getTypeOfHabitats()).doesNotContain(typeOfHabitatBack);
    }
}
