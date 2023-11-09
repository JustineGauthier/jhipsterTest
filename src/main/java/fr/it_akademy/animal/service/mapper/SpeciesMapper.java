package fr.it_akademy.animal.service.mapper;

import fr.it_akademy.animal.domain.Species;
import fr.it_akademy.animal.service.dto.SpeciesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Species} and its DTO {@link SpeciesDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpeciesMapper extends EntityMapper<SpeciesDTO, Species> {}
