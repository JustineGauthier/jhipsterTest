package fr.it_akademy.animal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.it_akademy.animal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpeciesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpeciesDTO.class);
        SpeciesDTO speciesDTO1 = new SpeciesDTO();
        speciesDTO1.setId(1L);
        SpeciesDTO speciesDTO2 = new SpeciesDTO();
        assertThat(speciesDTO1).isNotEqualTo(speciesDTO2);
        speciesDTO2.setId(speciesDTO1.getId());
        assertThat(speciesDTO1).isEqualTo(speciesDTO2);
        speciesDTO2.setId(2L);
        assertThat(speciesDTO1).isNotEqualTo(speciesDTO2);
        speciesDTO1.setId(null);
        assertThat(speciesDTO1).isNotEqualTo(speciesDTO2);
    }
}
