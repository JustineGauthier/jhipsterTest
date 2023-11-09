package fr.it_akademy.animal.service.mapper;

import fr.it_akademy.animal.domain.Animal;
import fr.it_akademy.animal.domain.TypeOfHabitat;
import fr.it_akademy.animal.service.dto.AnimalDTO;
import fr.it_akademy.animal.service.dto.TypeOfHabitatDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeOfHabitat} and its DTO {@link TypeOfHabitatDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeOfHabitatMapper extends EntityMapper<TypeOfHabitatDTO, TypeOfHabitat> {
    @Mapping(target = "animals", source = "animals", qualifiedByName = "animalIdSet")
    TypeOfHabitatDTO toDto(TypeOfHabitat s);

    @Mapping(target = "removeAnimal", ignore = true)
    TypeOfHabitat toEntity(TypeOfHabitatDTO typeOfHabitatDTO);

    @Named("animalId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AnimalDTO toDtoAnimalId(Animal animal);

    @Named("animalIdSet")
    default Set<AnimalDTO> toDtoAnimalIdSet(Set<Animal> animal) {
        return animal.stream().map(this::toDtoAnimalId).collect(Collectors.toSet());
    }
}
