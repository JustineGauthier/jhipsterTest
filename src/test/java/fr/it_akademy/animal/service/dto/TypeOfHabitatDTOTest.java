package fr.it_akademy.animal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.it_akademy.animal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeOfHabitatDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeOfHabitatDTO.class);
        TypeOfHabitatDTO typeOfHabitatDTO1 = new TypeOfHabitatDTO();
        typeOfHabitatDTO1.setId(1L);
        TypeOfHabitatDTO typeOfHabitatDTO2 = new TypeOfHabitatDTO();
        assertThat(typeOfHabitatDTO1).isNotEqualTo(typeOfHabitatDTO2);
        typeOfHabitatDTO2.setId(typeOfHabitatDTO1.getId());
        assertThat(typeOfHabitatDTO1).isEqualTo(typeOfHabitatDTO2);
        typeOfHabitatDTO2.setId(2L);
        assertThat(typeOfHabitatDTO1).isNotEqualTo(typeOfHabitatDTO2);
        typeOfHabitatDTO1.setId(null);
        assertThat(typeOfHabitatDTO1).isNotEqualTo(typeOfHabitatDTO2);
    }
}
