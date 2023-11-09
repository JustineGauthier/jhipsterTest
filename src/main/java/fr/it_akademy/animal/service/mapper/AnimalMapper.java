package fr.it_akademy.animal.service.mapper;

import fr.it_akademy.animal.domain.Animal;
import fr.it_akademy.animal.domain.Species;
import fr.it_akademy.animal.service.dto.AnimalDTO;
import fr.it_akademy.animal.service.dto.SpeciesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Animal} and its DTO {@link AnimalDTO}.
 */
@Mapper(componentModel = "spring")
public interface AnimalMapper extends EntityMapper<AnimalDTO, Animal> {
    @Mapping(target = "species", source = "species", qualifiedByName = "speciesId")
    AnimalDTO toDto(Animal s);

    @Named("speciesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SpeciesDTO toDtoSpeciesId(Species species);
}
