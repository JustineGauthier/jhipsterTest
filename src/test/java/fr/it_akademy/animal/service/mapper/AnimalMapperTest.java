package fr.it_akademy.animal.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class AnimalMapperTest {

    private AnimalMapper animalMapper;

    @BeforeEach
    public void setUp() {
        animalMapper = new AnimalMapperImpl();
    }
}
