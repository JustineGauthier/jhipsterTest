package fr.it_akademy.animal.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class SpeciesMapperTest {

    private SpeciesMapper speciesMapper;

    @BeforeEach
    public void setUp() {
        speciesMapper = new SpeciesMapperImpl();
    }
}
