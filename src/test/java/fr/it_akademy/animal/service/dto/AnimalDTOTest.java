package fr.it_akademy.animal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.it_akademy.animal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnimalDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnimalDTO.class);
        AnimalDTO animalDTO1 = new AnimalDTO();
        animalDTO1.setId(1L);
        AnimalDTO animalDTO2 = new AnimalDTO();
        assertThat(animalDTO1).isNotEqualTo(animalDTO2);
        animalDTO2.setId(animalDTO1.getId());
        assertThat(animalDTO1).isEqualTo(animalDTO2);
        animalDTO2.setId(2L);
        assertThat(animalDTO1).isNotEqualTo(animalDTO2);
        animalDTO1.setId(null);
        assertThat(animalDTO1).isNotEqualTo(animalDTO2);
    }
}
